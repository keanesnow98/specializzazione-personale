/** 
 * PrimeFaces Babylon Layout
 */
PrimeFaces.widget.Babylon = PrimeFaces.widget.BaseWidget.extend({

    init: function (cfg) {
        this._super(cfg);
        this.wrapper = $(document.body).children('.layout-wrapper');
        this.topbar = this.wrapper.children('.layout-topbar');
        this.menuButton = this.topbar.children('.layout-menu-button');
        this.menuContainer = this.wrapper.children('.layout-menu-container');
        this.menu = this.jq;
        this.menulinks = this.menu.find('a');
        this.expandedMenuitems = this.expandedMenuitems || [];

        this.topbarMenu = this.topbar.children('.topbar-menu');
        this.topbarItems = this.topbarMenu.children('li');
        this.topbarLinks = this.topbarItems.children('a');
        this.topbarMenuButton = $('#topbar-menu-button');

        this.topbarMenuClick = false;
        this.profileMenuClick = false;
        this.menuClick = false;
        this.menuActive = false;

        this.profileContainer = $('.layout-profile');
        this.profileButton = this.profileContainer.children('.layout-profile-button');
        this.profileMenu = this.profileContainer.children('.layout-profile-menu');


        this.configButton = $('#layout-config-button');
        this.configMenu = $('#layout-config');
        this.configMenuClose = this.configMenu.find('> .layout-config-content > .layout-config-close');

        this.restoreMenuState();
        this._bindEvents();
    },

    _bindEvents: function () {
        var $this = this;

        this.menuContainer.off('click.menu').on('click.menu', function () {
            if (!$this.profileMenuClick) {
                $this.menuClick = true;
            } 
        });

        this.menuButton.off('click.menu').on('click.menu', function (e) {
            $this.menuClick = true;

            if ($this.isMobile()) {
                $this.wrapper.toggleClass('layout-mobile-active');
                $(document.body).toggleClass('blocked-scroll');
            }
            else {
                if ($this.isStaticMenu()) {
                    $this.wrapper.toggleClass('layout-static-inactive');
                    $this.saveStaticMenuState();
                }
                else {
                    $this.wrapper.toggleClass('layout-overlay-active');
                }
            }

            e.preventDefault();
        });

        this.profileButton.off('click.profile').on('click.profile', function (e) {
            if (!$this.isHorizontalMenu() && !$this.isSlimMenu()) {
                $this.profileContainer.toggleClass('layout-profile-active');
                $this.profileMenu.slideToggle();
            }
            else {
                $this.profileMenuClick = true;

                if ($this.profileContainer.hasClass('layout-profile-active')) {
                    $this.profileMenu.addClass('fadeOutUp');

                    setTimeout(function () {
                        $this.profileContainer.removeClass('layout-profile-active');
                        $this.profileMenu.removeClass('fadeOutUp');
                    }, 150);
                }
                else {
                    $this.profileContainer.addClass('layout-profile-active');
                    $this.profileMenu.addClass('fadeInDown');
                }
            }

            e.preventDefault();
        });

        this.menu.find('> li').off('mouseenter.menu').on('mouseenter.menu', function(e) {    
            if (($this.isHorizontalMenu() || $this.isSlimMenu()) && $this.menuActive) {
                var item = $(this);
                
                if (!item.hasClass('active-menuitem')) {
                    $this.menu.find('.active-menuitem').removeClass('active-menuitem');
                    item.addClass('active-menuitem');
                }
            }
        });

        this.menulinks.off('click.menu').on('click.menu', function (e) {
            var link = $(this),
            item = link.parent('li'),
            submenu = item.children('ul');
            
            if ($this.isHorizontalMenu() || $this.isSlimMenu()) {
                submenu.css('display','');

                if (item.hasClass('active-menuitem')) {
                    if (submenu.length) {
                        item.removeClass('active-menuitem');
                        e.preventDefault();
                    }
    
                    if (item.parent().is($this.jq)) {
                        $this.menuActive = false;
                    }
                }
                else {
                    if (submenu.length > 0) {
                        e.preventDefault();
                    }
                    
                    item.siblings('.active-menuitem').removeClass('active-menuitem');
                    item.addClass('active-menuitem');
                    
                    if (item.parent().is($this.jq)) {
                        $this.menuActive = true;
                    }
                }
            }
            else {
                if (submenu.length) {
                    if (item.hasClass('active-menuitem')) {
                        $this.removeMenuitem(item.attr('id'));
                        
                        submenu.slideUp(400, function() {
                            item.removeClass('active-menuitem');
                        });
                    }
                    else {
                        $this.deactivateItems(item.siblings());
                        $this.addMenuitem(item.attr('id'));
                        $this.activate(item);
                    }
    
                    e.preventDefault();
                }
                else {
                    link.addClass('active-route');
                    $this.menu.find('.active-route').removeClass('active-route');
                    $this.setActiveRoute(item.attr('id'));
                    $.cookie('babylon_menu_scroll_state', link.attr('href') + ',' + $this.menuContainer.scrollTop(), { path: '/' });
                }
            }
        });

        this.topbarMenuButton.off('click.topbar').on('click.topbar', function (e) {
            $this.topbarMenuClick = true;
            $this.topbarMenu.find('ul').removeClass('fadeInDown fadeOutUp');

            if ($this.topbarMenu.hasClass('topbar-menu-visible'))
                $this.hideTopMenu();
            else 
                $this.showTopMenu();

            e.preventDefault();
        });

        this.topbarLinks.off('click.topbar').on('click.topbar', function (e) {
            var link = $(this),
            item = link.parent(),
            submenu = link.next();
            $this.topbarMenuClick = true;

            item.siblings('.active-topmenuitem').removeClass('active-topmenuitem');

            if ($this.isDesktop()) {
                if (submenu.length) {
                    if (item.hasClass('active-topmenuitem')) {
                        $this.hideTopBarSubMenu(item);
                    }
                    else {
                        item.addClass('active-topmenuitem');
                        submenu.addClass('fadeInDown');
                    }
                }
            }
            else {
                item.children('ul').removeClass('fadeInDown fadeOutUp');
                item.toggleClass('active-topmenuitem');
            }

            if (link.attr('href') === '#') {
                e.preventDefault();
            }
        });
        
        this.bindConfigEvents();

        $(document.body).off('click.layoutBody').on('click.layoutBody', function () {
            if (!$this.menuClick) {
                $this.wrapper.removeClass('layout-overlay-active layout-mobile-active');
                $(document.body).removeClass('blocked-scroll');

                if ($this.isHorizontalMenu() || $this.isSlimMenu()) {
                    $this.menu.find('.active-menuitem').removeClass('active-menuitem');
                    $this.menuActive = false;
                } 
            }

            if (!$this.topbarMenuClick) {
                $this.hideTopMenu();
                $this.hideTopBarSubMenu($this.topbarItems.filter('.active-topmenuitem'));
            }

            if (!$this.profileMenuClick && ($this.isHorizontalMenu() || $this.isSlimMenu())) {
                $this.profileContainer.removeClass('layout-profile-active');
                $this.profileMenu.css('display', '');
            }
            
            if (!$this.configMenuClicked) {
                $this.configMenu.removeClass('layout-config-active');
            }

            $this.menuClick = false;
            $this.topbarMenuClick = false;
            $this.profileMenuClick = false;
            $this.configMenuClicked = false;
        });
    },
    
    bindConfigEvents: function() {
        var $this = this;
        var changeConfigMenuState = function(e) {
            this.toggleClass(this.configMenu, 'layout-config-active');
            
            this.configMenuClicked = true;
            e.preventDefault();
        };
        
        this.configButton.off('click.config').on('click.config', changeConfigMenuState.bind(this));
        this.configMenuClose.off('click.config').on('click.config', changeConfigMenuState.bind(this));
        
        this.configMenu.off('click.rightpanel').on('click.rightpanel', function() {
            $this.configMenuClicked = true;
        });
    },

    hideTopMenu: function () {
        if (this.topbarMenu.hasClass('topbar-menu-visible')) {
            var $this = this;
            this.topbarMenu.addClass('fadeOutUp');
    
            setTimeout(function () {
                $this.topbarMenu.removeClass('fadeOutUp topbar-menu-visible');
            }, 175);
        }
    },

    hideTopBarSubMenu: function(item) {
        var submenu = item.children('ul');
        submenu.addClass('fadeOutUp');

        setTimeout(function () {
            item.removeClass('active-topmenuitem'),
            submenu.removeClass('fadeOutUp');
        }, 150);
    },

    showTopMenu: function() {
        this.topbarMenu.addClass('topbar-menu-visible fadeInDown');
    },
    
    toggleClass: function(el, className) {
        if (el.hasClass(className)) {
            el.removeClass(className);
        }
        else {
            el.addClass(className);
        }
    },

    activate: function (item) {
        var submenu = item.children('ul');
        item.addClass('active-menuitem');

        if (submenu.length && !this.isHorizontalMenu() && !this.isSlimMenu()) {
            submenu.slideDown();  
        }
    },

    deactivate: function (item) {
        var submenu = item.children('ul');
        item.removeClass('active-menuitem');

        if (submenu.length && !this.isHorizontalMenu() && !this.isSlimMenu()) {
            submenu.hide();  
        }
    },

    deactivateItems: function (items) {
        var $this = this;

        for (var i = 0; i < items.length; i++) {
            var item = items.eq(i),
                submenu = item.children('ul');

            if (submenu.length) {
                if (item.hasClass('active-menuitem')) {
                    var activeSubItems = item.find('.active-menuitem');
                    item.removeClass('active-menuitem');

                    submenu.slideUp('normal', function () {
                        $(this).parent().find('.active-menuitem').each(function () {
                            $this.deactivate($(this));
                        });
                    });

                    $this.removeMenuitem(item.attr('id'));
                    activeSubItems.each(function () {
                        $this.removeMenuitem($(this).attr('id'));
                    });
                }
                else {
                    item.find('.active-menuitem').each(function () {
                        var subItem = $(this);
                        $this.deactivate(subItem);
                        $this.removeMenuitem(subItem.attr('id'));
                    });
                }
            }
            else if (item.hasClass('active-menuitem')) {
                $this.deactivate(item);
                $this.removeMenuitem(item.attr('id'));
            }
        }
    },
        
    clearActiveItems: function() {
        var activeItems = this.jq.find('li.active-menuitem'),
        subContainers = activeItems.children('ul');

        activeItems.removeClass('active-menuitem');
        if(subContainers && subContainers.length) {
            subContainers.hide();
        }
    },

    clearLayoutState: function() {
        this.clearMenuState();
        this.clearActiveItems();
    },

    removeMenuitem: function (id) {
        this.expandedMenuitems = $.grep(this.expandedMenuitems, function (value) {
            return value !== id;
        });

        this.saveMenuState();
    },

    addMenuitem: function (id) {
        if ($.inArray(id, this.expandedMenuitems) === -1) {
            this.expandedMenuitems.push(id);
        }
        this.saveMenuState();
    },

    saveMenuState: function () {
        $.cookie('babylon_expandeditems', this.expandedMenuitems.join(','), { path: '/' });
    },

    saveScrollState: function (value) {
        $.cookie('babylon_scroll', value, { path: '/' });
    },

    clearMenuState: function () {
        this.expandedMenuitems = [];
        $.removeCookie('babylon_expandeditems', { path: '/' });
        $.removeCookie('babylon_active_route', { path: '/' });
        $.removeCookie('babylon_static_menu_inactive', { path: '/' });
        $.removeCookie('babylon_scroll', { path:'/' });
    },

    setActiveRoute: function (id) {
        $.cookie('babylon_active_route', id, { path: '/' });
    },

    saveStaticMenuState: function () {
        if (this.wrapper.hasClass('layout-static-inactive'))
            $.cookie('babylon_static_menu_inactive', 'babylon_static_menu_inactive', { path: '/' });
        else
            $.removeCookie('babylon_static_menu_inactive', { path: '/' });
    },

    isMobile: function () {
        return window.innerWidth <= 991;
    },

    isStaticMenu: function () {
        return this.wrapper.hasClass('layout-static') && this.isDesktop();
    },

    isHorizontalMenu: function() {
        return this.wrapper.hasClass('layout-horizontal') && this.isDesktop();
    },

    isSlimMenu: function() {
        return this.wrapper.hasClass('layout-slim') && this.isDesktop();
    },

    isDesktop: function () {
        return window.innerWidth > 991;
    },

    restoreMenuState: function () {
        var $this = this;
        var isHorizontalMenu = this.wrapper.hasClass('layout-horizontal');
        var isSlimMenu = this.wrapper.hasClass('layout-slim');

        if (!((isHorizontalMenu || isSlimMenu) && this.isDesktop())) {
            var menucookie = $.cookie('babylon_expandeditems');
            if (menucookie) {
                this.expandedMenuitems = menucookie.split(',');
                for (var i = 0; i < this.expandedMenuitems.length; i++) {
                    var id = this.expandedMenuitems[i];
                    if (id) {
                        var menuitem = $("#" + this.expandedMenuitems[i].replace(/:/g, "\\:"));
                        menuitem.addClass('active-menuitem');

                        var submenu = menuitem.children('ul');
                        if (submenu.length) {
                            submenu.show();
                        }
                    }
                }
            }

            var activeRoute = $.cookie('babylon_active_route');
            if (activeRoute) {
                var routeLink = $('#' + activeRoute.replace(/:/g, "\\:")).children('a');
                routeLink.addClass('active-route');

                setTimeout(function() {
                    $this.restoreScrollState(routeLink);
                }, 100);
            }

            var staticMenuCookie = $.cookie('babylon_static_menu_inactive');
            if (staticMenuCookie) {
                this.wrapper.addClass('layout-static-inactive layout-static-inactive-restore');
            }
        }
    },

    restoreScrollState: function(menuitem) {
        var scrollState = $.cookie('babylon_menu_scroll_state');
        if (scrollState) {
            var state = scrollState.split(',');
            if (state[0].startsWith(this.cfg.pathname) || this.isScrolledIntoView(menuitem, state[1])) {
                this.menuContainer.scrollTop(parseInt(state[1], 10));
            }
            else {
                this.scrollIntoView(menuitem.get(0));
                $.removeCookie('babylon_menu_scroll_state', { path: '/' });
            }
        }
        else if (!this.isScrolledIntoView(menuitem, menuitem.scrollTop())){
            this.scrollIntoView(menuitem.get(0));
        }
    },

    scrollIntoView: function(elem) {
        if (document.documentElement.scrollIntoView) {
            elem.scrollIntoView({ block: "nearest", inline: 'start' });

            var wrapper = $('.layout-menu-wrapper');
            var scrollTop = wrapper.scrollTop();
            if (scrollTop > 0) {
                wrapper.scrollTop(scrollTop + parseFloat(this.topbar.height()));
            }
        }
    },

    isScrolledIntoView: function(elem, scrollTop) {
        if (elem && elem.length) {
            var viewBottom = parseInt(scrollTop, 10) + this.menuContainer.height();

            var elemTop = elem.position().top;
            var elemBottom = elemTop + elem.height();

            return ((elemBottom <= viewBottom) && (elemTop >= scrollTop));
        }

        return false;
    }
});

PrimeFaces.BabylonConfigurator = {

    changeLayout: function(layoutTheme) {
        var linkElement = $('link[href*="layout-"]');
        var href = linkElement.attr('href');
        var startIndexOf = href.indexOf('layout-') + 7;
        var endIndexOf = href.indexOf('.css');
        var currentColor = href.substring(startIndexOf, endIndexOf);

        this.replaceLink(linkElement, href.replace(currentColor, layoutTheme));
    },

    changeComponentsTheme: function(theme) {
        var library = 'primefaces-babylon';
        var linkElement = $('link[href*="theme.css"]');
        var href = linkElement.attr('href');
        var index = href.indexOf(library) + 1;
        var currentTheme = href.substring(index + library.length);
        this.replaceLink(linkElement, href.replace(currentTheme, theme));
        this.changeLayout(theme);
    },

    changeSectionTheme: function(theme, section) {
        var wrapperElement = $('.layout-wrapper');
     
        var styleClass = wrapperElement.attr('class');
        var tokens = styleClass.split(' ');
        var sectionClass;
        for (var i = 0; i < tokens.length; i++) {
            if (tokens[i].indexOf(section + '-') > -1) {
                sectionClass = tokens[i];
                break;
            }
        }

        wrapperElement.attr('class', styleClass.replace(sectionClass, section + '-' + theme));
    },

    changeMenuMode: function(menuMode) {
        var wrapper = $(document.body).children('.layout-wrapper');
        switch (menuMode) {
            case 'layout-static layout-static-active':
                wrapper.addClass('layout-static layout-static-active').removeClass('layout-overlay layout-slim layout-horizontal ');
                this.clearLayoutState();
            break;

            case 'layout-overlay':
                wrapper.addClass('layout-overlay').removeClass('layout-static layout-slim layout-horizontal  layout-static-active');
                this.clearLayoutState();
            break;

            case 'layout-horizontal':
                wrapper.addClass('layout-horizontal').removeClass('layout-static layout-overlay  layout-slim  layout-static-active');
                this.clearLayoutState();
            break;

            case 'layout-slim':
                wrapper.addClass('layout-slim').removeClass('layout-static layout-overlay layout-horizontal layout-static-active');
                this.clearLayoutState();
            break;

            default:
                wrapper.addClass('layout-static').removeClass('layout-overlay layout-slim layout-horizontal ');
                this.clearLayoutState();
            break;
        }
    },

    beforeResourceChange: function() {
        PrimeFaces.ajax.RESOURCE = null;    //prevent resource append
    },
    
    replaceLink: function(linkElement, href) {
        PrimeFaces.ajax.RESOURCE = 'javax.faces.Resource';

        var isIE = this.isIE();

        if (isIE) {
            linkElement.attr('href', href);
        }
        else {
            var cloneLinkElement = linkElement.clone(false);

            cloneLinkElement.attr('href', href);
            linkElement.after(cloneLinkElement);
            
            cloneLinkElement.off('load').on('load', function() {
                linkElement.remove();
            });
        }
    },

    changeMenuToRTL: function() {
        var wrapper = $('.layout-wrapper');
        wrapper.toggleClass('layout-rtl');
    },

    clearLayoutState: function() {
        var menu = PF('babylonMenuWidget');

        if (menu) {
            menu.clearLayoutState();
        }
    },
    
    isIE: function() {
        return /(MSIE|Trident\/|Edge\/)/i.test(navigator.userAgent);
    },

    updateInputStyle: function(value) {
        if (value === 'filled')
            $(document.body).addClass('ui-input-filled');
        else
            $(document.body).removeClass('ui-input-filled');
    }
};

/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2006, 2014 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        // AMD (Register as an anonymous module)
        define(['jquery'], factory);
    } else if (typeof exports === 'object') {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(jQuery);
    }
}(function ($) {

    var pluses = /\+/g;

    function encode(s) {
        return config.raw ? s : encodeURIComponent(s);
    }

    function decode(s) {
        return config.raw ? s : decodeURIComponent(s);
    }

    function stringifyCookieValue(value) {
        return encode(config.json ? JSON.stringify(value) : String(value));
    }

    function parseCookieValue(s) {
        if (s.indexOf('"') === 0) {
            // This is a quoted cookie as according to RFC2068, unescape...
            s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
        }

        try {
            // Replace server-side written pluses with spaces.
            // If we can't decode the cookie, ignore it, it's unusable.
            // If we can't parse the cookie, ignore it, it's unusable.
            s = decodeURIComponent(s.replace(pluses, ' '));
            return config.json ? JSON.parse(s) : s;
        } catch (e) { }
    }

    function read(s, converter) {
        var value = config.raw ? s : parseCookieValue(s);
        return $.isFunction(converter) ? converter(value) : value;
    }

    var config = $.cookie = function (key, value, options) {

        // Write

        if (arguments.length > 1 && !$.isFunction(value)) {
            options = $.extend({}, config.defaults, options);

            if (typeof options.expires === 'number') {
                var days = options.expires, t = options.expires = new Date();
                t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
            }

            return (document.cookie = [
                encode(key), '=', stringifyCookieValue(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path ? '; path=' + options.path : '',
                options.domain ? '; domain=' + options.domain : '',
                options.secure ? '; secure' : ''
            ].join(''));
        }

        // Read

        var result = key ? undefined : {},
            // To prevent the for loop in the first place assign an empty array
            // in case there are no cookies at all. Also prevents odd result when
            // calling $.cookie().
            cookies = document.cookie ? document.cookie.split('; ') : [],
            i = 0,
            l = cookies.length;

        for (; i < l; i++) {
            var parts = cookies[i].split('='),
                name = decode(parts.shift()),
                cookie = parts.join('=');

            if (key === name) {
                // If second argument (value) is a function it's a converter...
                result = read(cookie, value);
                break;
            }

            // Prevent storing a cookie that we couldn't decode.
            if (!key && (cookie = read(cookie)) !== undefined) {
                result[name] = cookie;
            }
        }

        return result;
    };

    config.defaults = {};

    $.removeCookie = function (key, options) {
        // Must not alter options, thus extending a fresh object...
        $.cookie(key, '', $.extend({}, options, { expires: -1 }));
        return !$.cookie(key);
    };

}));

if (PrimeFaces.widget.InputSwitch) {
    PrimeFaces.widget.InputSwitch = PrimeFaces.widget.InputSwitch.extend({

        init: function (cfg) {
            this._super(cfg);

            if (this.input.prop('checked')) {
                this.jq.addClass('ui-inputswitch-checked');
            }
        },

        check: function () {
            var $this = this;

            this.input.prop('checked', true).trigger('change');
            setTimeout(function () {
                $this.jq.addClass('ui-inputswitch-checked');
            }, 100);
        },

        uncheck: function () {
            var $this = this;

            this.input.prop('checked', false).trigger('change');
            setTimeout(function () {
                $this.jq.removeClass('ui-inputswitch-checked');
            }, 100);
        }
    });
}

if (PrimeFaces.widget.TriStateCheckbox) {
    PrimeFaces.widget.TriStateCheckbox = PrimeFaces.widget.TriStateCheckbox.extend({

        init: function (cfg) {
            this._super(cfg);

            this.jq.addClass('ui-tristatecheckbox');
        }
    });
}

/* Issue #924 is fixed for 5.3+ and 6.0. (compatibility with 5.3) */
if(window['PrimeFaces'] && window['PrimeFaces'].widget.Dialog) {
    PrimeFaces.widget.Dialog = PrimeFaces.widget.Dialog.extend({

        enableModality: function() {
            this._super();
            $(document.body).children(this.jqId + '_modal').addClass('ui-dialog-mask');
        },

        syncWindowResize: function() {}
    });
}

if (PrimeFaces.widget.SelectOneMenu) {
    PrimeFaces.widget.SelectOneMenu = PrimeFaces.widget.SelectOneMenu.extend({
        init: function (cfg) {
            this._super(cfg);

            var $this = this;
            if (this.jq.parent().hasClass('ui-float-label')) {
                this.m_panel = $(this.jqId + '_panel');
                this.m_focusInput = $(this.jqId + '_focus');

                this.m_panel.addClass('ui-input-overlay-panel');
                this.jq.addClass('ui-inputwrapper');

                if (this.input.val() != '') {
                    this.jq.addClass('ui-inputwrapper-filled');
                }

                this.input.off('change').on('change', function () {
                    $this.inputValueControl($(this));
                });

                this.m_focusInput.on('focus.ui-selectonemenu', function () {
                    $this.jq.addClass('ui-inputwrapper-focus');
                })
                    .on('blur.ui-selectonemenu', function () {
                        $this.jq.removeClass('ui-inputwrapper-focus');
                    });

                if (this.cfg.editable) {
                    this.label.on('input', function (e) {
                        $this.inputValueControl($(this));
                    }).on('focus', function () {
                        $this.jq.addClass('ui-inputwrapper-focus');
                    }).on('blur', function () {
                        $this.jq.removeClass('ui-inputwrapper-focus');
                        $this.inputValueControl($(this));
                    });
                }
            }
        },

        inputValueControl: function (input) {
            if (input.val() != '')
                this.jq.addClass('ui-inputwrapper-filled');
            else
                this.jq.removeClass('ui-inputwrapper-filled');
        }
    });
}

if (PrimeFaces.widget.Chips) {
    PrimeFaces.widget.Chips = PrimeFaces.widget.Chips.extend({
        init: function (cfg) {
            this._super(cfg);

            var $this = this;
            if (this.jq.parent().hasClass('ui-float-label')) {
                this.jq.addClass('ui-inputwrapper');

                if ($this.jq.find('.ui-chips-token').length !== 0) {
                    this.jq.addClass('ui-inputwrapper-filled');
                }

                this.input.on('focus.ui-chips', function () {
                    $this.jq.addClass('ui-inputwrapper-focus');
                }).on('input.ui-chips', function () {
                    $this.inputValueControl();
                }).on('blur.ui-chips', function () {
                    $this.jq.removeClass('ui-inputwrapper-focus');
                    $this.inputValueControl();
                });

            }
        },

        inputValueControl: function () {
            if (this.jq.find('.ui-chips-token').length !== 0 || this.input.val() != '')
                this.jq.addClass('ui-inputwrapper-filled');
            else
                this.jq.removeClass('ui-inputwrapper-filled');
        }
    });
}

if (PrimeFaces.widget.DatePicker) {
    PrimeFaces.widget.DatePicker = PrimeFaces.widget.DatePicker.extend({
        init: function (cfg) {
            this._super(cfg);

            var $this = this;
            if (this.jq.parent().hasClass('ui-float-label') && !this.cfg.inline) {
                if (this.input.val() != '') {
                    this.jq.addClass('ui-inputwrapper-filled');
                }

                this.jqEl.off('focus.ui-datepicker blur.ui-datepicker change.ui-datepicker')
                    .on('focus.ui-datepicker', function () {
                        $this.jq.addClass('ui-inputwrapper-focus');
                    })
                    .on('blur.ui-datepicker', function () {
                        $this.jq.removeClass('ui-inputwrapper-focus');
                    })
                    .on('change.ui-datepicker', function () {
                        $this.inputValueControl($(this));
                    });
            }
        },

        inputValueControl: function (input) {
            if (input.val() != '')
                this.jq.addClass('ui-inputwrapper-filled');
            else
                this.jq.removeClass('ui-inputwrapper-filled');
        }
    });
}