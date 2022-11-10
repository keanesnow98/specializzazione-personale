package it.antonio.specializzazionepersonale.menu;

public class Menu {

	public static String menu(String page) {
		String isActiveDashboard = "inactive";
		String isActiveAnagVVF = "inactive";
		String isActiveProd = "inactive";

		//IS SELECTED
		if (page.equals("dashboard") || page.equals("home")) {
			isActiveDashboard = "active";
		}else if (page.equals("anagraficavvf")){
			isActiveAnagVVF = "active";
		}else if (page.equals("products")){
			isActiveProd = "active";
		};
		
		//MENU
		String menu = "<nav id=\"sidebarMenu\" class=\"col-md-3 col-lg-2 d-md-block bg-light sidebar collapse\">\r\n"
		    		+ "      <div class=\"position-sticky pt-3\">\r\n"
		    		+ "        <ul class=\"nav flex-column\">\r\n";
			
		String dashboard =      "<li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link " + isActiveDashboard + "\" aria-current=\"page\" href=\"/home\">\r\n"
		    		+ "              <span data-feather=\"home\"></span>\r\n"
		    		+ "              Dashboard\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n";
		
		String anagraficavvf =   "<li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link " + isActiveAnagVVF + "\" href=\"/anagraficavvf\">\r\n"
		    		+ "              <span data-feather=\"users\"></span>\r\n"
		    		+ "              Anagrafica VVF\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n";
		    			
		String products =        "<li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link " + isActiveProd + "\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"file\"></span>\r\n"
		    		+ "              Specializzazioni\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n";
		
		String menu2part =       "<li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"file\"></span>\r\n"
		    		+ "              Qualifiche\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "          <li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"bar-chart-2\"></span>\r\n"
		    		+ "              Reports\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "          <li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"layers\"></span>\r\n"
		    		+ "              Integrations\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "        </ul>\r\n"
		    		+ "\r\n"
		    		+ "        <h6 class=\"sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted\">\r\n"
		    		+ "          <span>Saved reports</span>\r\n"
		    		+ "          <a class=\"link-secondary\" href=\"#\" aria-label=\"Add a new report\">\r\n"
		    		+ "            <span data-feather=\"plus-circle\"></span>\r\n"
		    		+ "          </a>\r\n"
		    		+ "        </h6>\r\n"
		    		+ "        <ul class=\"nav flex-column mb-2\">\r\n"
		    		+ "          <li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"file-text\"></span>\r\n"
		    		+ "              Stampa Elenco VVF\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "          <li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"file-text\"></span>\r\n"
		    		+ "              Stampa per Turni\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "          <li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"file-text\"></span>\r\n"
		    		+ "              Stampa Spec.\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "          <li class=\"nav-item\">\r\n"
		    		+ "            <a class=\"nav-link\" href=\"#\">\r\n"
		    		+ "              <span data-feather=\"file-text\"></span>\r\n"
		    		+ "              Stampa Scadenze\r\n"
		    		+ "            </a>\r\n"
		    		+ "          </li>\r\n"
		    		+ "        </ul>\r\n"
		    		+ "      </div>\r\n"
		    		+ "    </nav>";
		return menu+dashboard+anagraficavvf+products+menu2part;
	}
	
}
