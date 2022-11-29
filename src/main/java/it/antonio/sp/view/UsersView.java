package it.antonio.sp.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;

import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.service.UserService;
import it.antonio.sp.util.Constants;
import it.antonio.sp.util.Generator;

@ManagedBean
@ViewScoped
public class UsersView {
	private List<UserEntity> users;
	private UserEntity selectedUser;
	private DualListModel<String> selectedUserRoles;
	
	@Autowired
	UserService userService;
	
	private List<String> rolesSource;
	
	public List<UserEntity> getUsers() {
		return users;
	}
	
	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}
	
	public UserEntity getSelectedUser() {
		return selectedUser;
	}
	
	public void setSelectedUser(UserEntity selectedUser) {
		this.selectedUser = selectedUser;
	}
	
	public DualListModel<String> getSelectedUserRoles() {
		return selectedUserRoles;
	}
	
	public void setSelectedUserRoles(DualListModel<String> selectedUserRoles) {
		this.selectedUserRoles = selectedUserRoles;
	}
	
	public String getGravatarURI(String email) {
		return "https://www.gravatar.com/avatar/" + Generator.md5Hex(email) + "?s=200&r=pg";
	}
	
	public void setSelectedUserRoles(List<String> rolesTarget) {
		List<String> rolesSource = this.rolesSource;
		rolesSource.removeAll(rolesTarget);
		selectedUserRoles = new DualListModel<String>(rolesSource, rolesTarget);
	}
	
	@PostConstruct
	public void init() {
		users = userService.findAll();
		rolesSource = new ArrayList<String>();
		rolesSource.add(Constants.ROLE_ADMIN);
		rolesSource.add(Constants.ROLE_ADMIN_SPECIALTIES_ALL);
		rolesSource.add(Constants.ROLE_ADMIN_QUALIFICATIONS_ALL);
		rolesSource.add(Constants.ROLE_ADMIN_USER_MANAGEMENT_ALL);
		
		rolesSource.add(Constants.ROLE_USER_DASHBOARD_VIEW);
		rolesSource.add(Constants.ROLE_USER_REPORTS_BY_SPECIALTIES_ALL);
		rolesSource.add(Constants.ROLE_USER_REPORTS_BY_TURNO_ALL);
		
		rolesSource.add(Constants.ROLE_USER_ANAGRAPHICVVF_VIEW);
		rolesSource.add(Constants.ROLE_USER_ANAGRAPHICVVF_INSERT);
		rolesSource.add(Constants.ROLE_USER_ANAGRAPHICVVF_EDIT);
		rolesSource.add(Constants.ROLE_USER_ANAGRAPHICVVF_DELETE);
		
		rolesSource.add(Constants.ROLE_USER_SPECIALTY_VIEW);
		rolesSource.add(Constants.ROLE_USER_SPECIALTY_INSERT);
		rolesSource.add(Constants.ROLE_USER_SPECIALTY_EDIT);
		rolesSource.add(Constants.ROLE_USER_SPECIALTY_DELETE);
	}
	
	public void saveUser() {
		selectedUser.setRoles(selectedUserRoles.getTarget());
		userService.updateUser(selectedUser);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Saved"));

        PrimeFaces.current().executeScript("PF('modifyUserDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dv-users");
    }
	
	public void deleteUser() {
		users.remove(selectedUser);
        userService.deleteUser(selectedUser);
        this.selectedUser = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Removed"));
        PrimeFaces.current().ajax().update("form:messages", "form:dv-users");
    }
}
