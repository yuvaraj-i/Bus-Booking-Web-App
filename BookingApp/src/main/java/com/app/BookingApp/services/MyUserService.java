package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.app.BookingApp.configuration.JwtTokenUtils;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.MyUserDetails;
import com.app.BookingApp.models.Orders;
import com.app.BookingApp.models.Roles;
import com.app.BookingApp.models.Ticket;
import com.app.BookingApp.models.UserProfile;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.OrderReposistory;
import com.app.BookingApp.repository.PassengerReposistory;
import com.app.BookingApp.repository.RolesRepository;
import com.app.BookingApp.repository.TicketReposistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserService implements UserDetailsService {

    private MyUserRespository userRespository;
    private RolesRepository rolesRepository;
    private JwtTokenUtils jwtTokenUtils;
    private OrderReposistory orderReposistory;
    private TicketReposistory ticketReposistory;
    private PassengerReposistory passengerReposistory;

    @Autowired
    public MyUserService(MyUserRespository userRespository, RolesRepository rolesRepository,
            JwtTokenUtils jwtTokenUtils, OrderReposistory orderReposistory, TicketReposistory ticketReposistory,
            PassengerReposistory passengerReposistory) {
        this.userRespository = userRespository;
        this.rolesRepository = rolesRepository;
        this.jwtTokenUtils = jwtTokenUtils;
        this.orderReposistory = orderReposistory;
        this.ticketReposistory = ticketReposistory;
        this.passengerReposistory = passengerReposistory;
    }

    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return null;
        }

        MyUser user = optionalUser.get();
        Iterable<Roles> userRoles = rolesRepository.findByUserId(user.getId());
        MyUserDetails userWithAuthorithy = new MyUserDetails(user, userRoles);

        return userWithAuthorithy;
    }

    public List<UserProfile> getAllUsers() {
        List<UserProfile> allUserList = new ArrayList<>();
        Iterable<MyUser> allUserDatasList = userRespository.findAll();
        Iterator<MyUser> userIterator = allUserDatasList.iterator();

        while (userIterator.hasNext()) {
            MyUser userData = userIterator.next();
            UserProfile userResponse = new UserProfile();

            userResponse.setName(userData.getName());
            userResponse.setAge(userData.getAge());
            userResponse.setDateOfBirth(userData.getDateOfBirth());
            userResponse.setEmailAddress(userData.getEmailAddress());
            userResponse.setMobileNumber(userData.getMobileNumber());
            userResponse.setEnable(userData.isEnabled());

            ArrayList<String> userRoles = getListRoles(userData.getId());
            userResponse.setRoles(userRoles);

            allUserList.add(userResponse);
        }

        return allUserList;
    }

    public ResponseEntity<Object> getUserById(HttpServletRequest request) {
        String mobileNumber = getUserId(request);
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);
        ArrayList<String> userRoles = new ArrayList<>();

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User Not Found", HttpStatus.BAD_REQUEST);

        }

        MyUser user = optionalUser.get();
        UserProfile userProfile = new UserProfile();

        userProfile.setDateOfBirth(user.getDateOfBirth());
        userProfile.setEmailAddress(user.getEmailAddress());
        userProfile.setMobileNumber(user.getMobileNumber());
        userProfile.setName(user.getName());
        userProfile.setAge(user.getAge());
        userProfile.setEnable(user.isEnabled());

        Iterable<Roles> userRolesList = rolesRepository.findByUserId(user.getId());
        Iterator<Roles> userRolesListIterator = userRolesList.iterator();

        while (userRolesListIterator.hasNext()) {
            Roles userRole = userRolesListIterator.next();

            userRoles.add(userRole.getRole());
        }

        userProfile.setRoles(userRoles);

        return new ResponseEntity<Object>(userProfile, HttpStatus.OK);

    }

    public Optional<MyUser> getUserByMobileNumber(String mobileNumber) {
        return userRespository.findUserByMobileNumber(mobileNumber);
    }

    public void deleteExtistingUser(Long id) {
        boolean iSUserExist = userRespository.existsById(id);

        if (!iSUserExist) {
            throw new IllegalStateException("user with " + id + " not found!");
        }

        userRespository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long id, String name, String emailAddress) {
        Optional<MyUser> user = userRespository.findById(id);

        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

    }

    public String getUserId(HttpServletRequest request) {
        String jwtToken = "";

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("Authorization_1")) {
                    jwtToken = cookie.getValue();
                }
            }
        }

        String mobileNumber = jwtTokenUtils.getUserMobileNumber(jwtToken);

        return mobileNumber;
    }

    public ResponseEntity<Object> getUserRoles(HttpServletRequest request) {

        ArrayList<String> rolesList = new ArrayList<>();

        String mobileNumber = getUserId(request);
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User not found", HttpStatus.BAD_REQUEST);
        }

        Long userId = optionalUser.get().getId();
        Iterable<Roles> roles = rolesRepository.findByUserId(userId);
        Iterator<Roles> rolesIterator = roles.iterator();

        while (rolesIterator.hasNext()) {
            Roles userRoles = rolesIterator.next();
            rolesList.add(userRoles.getRole());
        }

        return new ResponseEntity<Object>(rolesList, HttpStatus.OK);
    }

    public ArrayList<String> getListRoles(Long userId) {
        ArrayList<String> rolesList = new ArrayList<>();
        Iterable<Roles> roles = rolesRepository.findByUserId(userId);
        Iterator<Roles> rolesIterator = roles.iterator();

        while (rolesIterator.hasNext()) {
            Roles userRoles = rolesIterator.next();
            rolesList.add(userRoles.getRole());
        }

        return rolesList;
    }

    @Transactional
    public ResponseEntity<Object> setUserInActive(String mobileNumber) {
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User not found", HttpStatus.BAD_REQUEST);

        }

        MyUser user = optionalUser.get();
        Iterable<Roles> rolesList = rolesRepository.findByUserId(user.getId());
        Iterator<Roles> rolesListIterator = rolesList.iterator();

        while (rolesListIterator.hasNext()) {
            Roles userRole = rolesListIterator.next();
            if (userRole.getRole().equals("admin")) {
                return new ResponseEntity<Object>("cann't be disable admin user", HttpStatus.BAD_REQUEST);

            }
        }

        userAccountConfig(user, false);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<Object> setUserActive(String mobileNumber) {
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User not found", HttpStatus.BAD_REQUEST);

        }

        MyUser user = optionalUser.get();
        userAccountConfig(user, true);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

    private void userAccountConfig(MyUser user, boolean status) {

        if (user.isEnabled() != status) {
            user.setEnabled(status);

        }

    }

    @Transactional
    public ResponseEntity<Object> deleteUser(String mobileNumber) {

        // order
        // passenger
        // ticket
        // roles
        // user
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User not found", HttpStatus.BAD_REQUEST);
        }

        MyUser user = optionalUser.get();

        Iterable<Roles> userRoles = rolesRepository.findByUserId(user.getId());
        Iterator<Roles> userRolesIterator = userRoles.iterator();

        while (userRolesIterator.hasNext()) {
            Roles userRole = userRolesIterator.next();

            if (userRole.getRole().equals("admin")) {
                return new ResponseEntity<Object>("Admin can not be deleted", HttpStatus.BAD_REQUEST);
            }

        }

        rolesRepository.deleteAll(userRoles);

        Iterable<Orders> userBookings = orderReposistory.findAllByUserId(user.getId());
        Iterator<Orders> bookingsIterator = userBookings.iterator();

        while (bookingsIterator.hasNext()) {
            Orders bookedTickets = bookingsIterator.next();
            Ticket userTicket = bookedTickets.getTicket();

            userTicket.setBus(null);
            ticketReposistory.delete(userTicket);
            passengerReposistory.deleteAll(passengerReposistory.findByTicketId(userTicket.getId()));
            orderReposistory.delete(bookedTickets);
        }

        userRespository.delete(user);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);
    }

}
