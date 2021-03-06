package org.wintersleep.usermgmt.model.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wintersleep.usermgmt.model.*;
import org.apache.log4j.Logger;

import java.util.List;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

    private static final Logger log = Logger.getLogger(UserManagementServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private UserManagementService selfProxy;

    public void setSelfProxy(UserManagementService selfProxy) {
        this.selfProxy = selfProxy;
    }

    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        log.debug("Found " + users.size() + " users.");
        for (User user : users) {
            log.debug(user.getFullName());
        }
        return users;
    }

    public List<User> findUsers(int firstResult, int maxResults) {
        return userRepository.find(firstResult, maxResults);
    }

    public void saveUser(User user) {
        userRepository.makePersistent(user);
    }

    public Role findRole(Long id) {
        return roleRepository.findById(id);
    }

    public String getState() {
        return System.identityHashCode(this) + ":" + System.identityHashCode(selfProxy);
    }

    public User findUser(long id) {
        return userRepository.findById(id);
    }

    public List<UserProfile> findProfiles() {
        return userProfileRepository.findAll();
    }

    public long countUsers() {
        return userRepository.countAll();
    }

}