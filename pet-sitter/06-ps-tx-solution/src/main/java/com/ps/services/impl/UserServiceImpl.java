package com.ps.services.impl;

import com.ps.ents.User;
import com.ps.exceptions.MailSendingException;
import com.ps.repos.UserRepo;
import com.ps.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public User findById(Long id) {
        logger.debug(">>> Preparing to execute SERVICE.findById");
        User user = userRepo.findById(id);
        logger.debug(">>> Done executing REPO.findById");
        int i = countUsers(); // called not in transaction
        logger.debug(">>> i" + i);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public int countUsers() {
        return userRepo.countUsers();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void htmlAllByNameAll(String name) {
        userRepo.htmlAllByName(name);
    }

    @Transactional(rollbackFor = MailSendingException.class)
    @Override
    public int updatePassword(Long userId, String newPass) throws MailSendingException {
        User u = userRepo.findById(userId);
        String email = u.getEmail();
        int result = userRepo.updatePassword(userId, newPass);
        sendEmail(email);
        return result;
    }

    private void sendEmail(String email) throws MailSendingException {
        if (true) {
            throw new MailSendingException("Configrmation email for password could not be sent. Password was not send.");
        }
    }
}
