package com.app.BookingApp.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.app.BookingApp.models.BookingResponse;
import com.app.BookingApp.models.Bus;
import com.app.BookingApp.models.MyUser;
import com.app.BookingApp.models.Orders;
import com.app.BookingApp.models.Passenger;
import com.app.BookingApp.models.Seat;
import com.app.BookingApp.models.Ticket;
import com.app.BookingApp.models.UserBookingDetailsRequest;
import com.app.BookingApp.repository.BusRespository;
import com.app.BookingApp.repository.MyUserRespository;
import com.app.BookingApp.repository.OrderReposistory;
import com.app.BookingApp.repository.PassengerReposistory;
import com.app.BookingApp.repository.SeatRepository;
import com.app.BookingApp.repository.TicketReposistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private MyUserRespository userRespository;
    private BusRespository busRespository;
    private TicketReposistory ticketReposistory;
    private OrderReposistory orderReposistory;
    private MyUserService userService;
    private BusService busService;
    private SeatRepository seatRepository;
    private PassengerReposistory passengerReposistory;

    @Autowired
    public BookingService(MyUserRespository userRespository, BusRespository busRespository,
            TicketReposistory ticketReposistory, OrderReposistory orderReposistory,
            BusService busService, SeatRepository seatRepository, MyUserService userService, 
            PassengerReposistory passengerReposistory) {
        this.userRespository = userRespository;
        this.busRespository = busRespository;
        this.ticketReposistory = ticketReposistory;
        this.orderReposistory = orderReposistory;
        this.busService = busService;
        this.seatRepository = seatRepository;
        this.userService = userService;
        this.passengerReposistory = passengerReposistory;
    }

    public ResponseEntity<Object> bookTicketForUser(UserBookingDetailsRequest userBookingDetails, Long busId) {

        String userEmail = userBookingDetails.getEmail();
        ArrayList<Passenger> passengersList = userBookingDetails.getPassengers();

        Optional<MyUser> optionalUser = userRespository.findUserByEmailAddress(userEmail);
        Optional<Bus> optionalBus = busRespository.findById(busId);

        if ((!optionalUser.isPresent())) {
            return new ResponseEntity<Object>("user name or email is not a registered", HttpStatus.BAD_REQUEST);
        }

        else if (!optionalBus.isPresent()) {
            return new ResponseEntity<Object>("invalid bus selection", HttpStatus.BAD_REQUEST);
        }

        if (userBookingDetails.getSeatNumbers().size() == 0) {
            return new ResponseEntity<Object>("seats not selected", HttpStatus.BAD_REQUEST);
        }

        MyUser user = optionalUser.get();
        Bus bus = optionalBus.get();

        setAvaliabilty(busId, userBookingDetails.getSeatNumbers());
        int bookingCharges = busService.calculateBusCharges(bus, userBookingDetails.getSeatNumbers().size());

        Ticket ticket = new Ticket();
        ticket.setBus(bus);
        ticket.setSeatNumbers(userBookingDetails.getSeatNumbers());
        ticket.setBusCharges(bookingCharges);
        ticket.setBookedDate(LocalDate.now());
        ticket.setTravellingDate(userBookingDetails.getTravelingDate());
        Ticket ticketSave = ticketReposistory.save(ticket);

        ArrayList<Passenger> savePassengersList = new ArrayList<>();
        
        for(int index = 0; index < passengersList.size(); index++) {
            Passenger passengerDetails = passengersList.get(index);

            Passenger passenger = new Passenger();
            passenger.setName(passengerDetails.getName());
            passenger.setGender(passengerDetails.getGender());
            passenger.setTicket(ticketSave);
            Passenger savedPassenger = passengerReposistory.save(passenger);

            savePassengersList.add(savedPassenger);
        }

        Orders userOrders = new Orders();
        userOrders.setTicket(ticketSave);
        userOrders.setUser(user);
        orderReposistory.save(userOrders);

        return new ResponseEntity<Object>("SUCCESS", HttpStatus.OK);

    }

    private void setAvaliabilty(Long busId, ArrayList<Integer> seatNumbersList) {
        Iterable<Seat> busSeatsCollections = seatRepository.findByBusId(busId);
        Iterator<Seat> seatIterator = busSeatsCollections.iterator();

        while (seatIterator.hasNext()) {
            Seat busSeat = seatIterator.next();

            for (int index = 0; index < seatNumbersList.size(); index++) {
                if (busSeat.getSeatNo() == seatNumbersList.get(index)) {
                    busSeat.setIsAvaliable(false);
                    seatRepository.save(busSeat);
                    break;
                }
            }

        }

    }

    public ResponseEntity<Object> getUserBookingDetils(HttpServletRequest request) {
        String mobileNumber = userService.getUserId(request);
        Optional<MyUser> optionalUser = userRespository.findUserByMobileNumber(mobileNumber);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<Object>("User not found", HttpStatus.BAD_REQUEST);
        }

        Long userId = optionalUser.get().getId();
        List<BookingResponse> userTickets = new ArrayList<>();
        Iterable<Orders> usersBookings = orderReposistory.findAllByUserId(userId);
        Iterator<Orders> usersBookingsIterator = usersBookings.iterator();
        int count = 0;

        while (usersBookingsIterator.hasNext()) {
            count += 1;
            Orders userOrder = usersBookingsIterator.next();
            Ticket busTicket = userOrder.getTicket();
            Bus bookedBus = busTicket.getBus();
            Iterable<Passenger> passengersListEntity = passengerReposistory.findByTicketId(busTicket.getId());
            Iterator<Passenger> passengerIterator = passengersListEntity.iterator();
            ArrayList<Passenger> passengersList = new ArrayList<>();

            while (passengerIterator.hasNext()) {
                Passenger passenger = passengerIterator.next();

                Passenger responsePassenger = new Passenger();
                responsePassenger.setName(passenger.getName());
                responsePassenger.setGender(passenger.getGender());
                passengersList.add(responsePassenger);
            }

            BookingResponse bookingDetails = new BookingResponse();
            bookingDetails.setBusName(bookedBus.getBusName());
            bookingDetails.setDestinationLocation(bookedBus.getEndLocation());
            bookingDetails.setBoardingLocation(bookedBus.getStartLocation());
            bookingDetails.setPickupPoint(bookedBus.getPickupPoint());
            bookingDetails.setDropingPoint(bookedBus.getDropingPoint());
            bookingDetails.setBookingDate(busTicket.getBookedDate());
            bookingDetails.setTravelingDate(busTicket.getTravellingDate());
            bookingDetails.setPassengers(passengersList);
            bookingDetails.setSeatNumbers(busTicket.getSeatNumbers());
            bookingDetails.setCharges(busTicket.getBusCharges());
            bookingDetails.setTicketId(busTicket.getId());
            bookingDetails.setBusType(bookedBus.getType());

            userTickets.add(bookingDetails);
        }

        if (count == 0) {
            return new ResponseEntity<Object>("Booking History is empty", HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<Object>(userTickets, HttpStatus.OK);

    }

}
