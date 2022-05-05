package com.app.BookingApp.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
    private BusService busService;
    private SeatRepository seatRepository;

    @Autowired
    public BookingService(MyUserRespository userRespository, BusRespository busRespository,
            TicketReposistory ticketReposistory, OrderReposistory orderReposistory,
            BusService busService, SeatRepository seatRepository) {
        this.userRespository = userRespository;
        this.busRespository = busRespository;
        this.ticketReposistory = ticketReposistory;
        this.orderReposistory = orderReposistory;
        this.busService = busService;
        this.seatRepository = seatRepository;
    }

    public ResponseEntity<Object> bookTicketForUser(UserBookingDetailsRequest userBookingDetails, Long busId) {        
        String userEmail = userBookingDetails.getEmail();
        List<Passenger> listPassengers = userBookingDetails.getPassengers();

        Optional<MyUser> optionalUser = userRespository.findUserByEmailAddress(userEmail);
        Optional<Bus> optionalBus = busRespository.findById(busId);

        if ((!optionalUser.isPresent()) && (!optionalBus.isPresent())) {

            return new ResponseEntity<Object>("user name or email is not a registered", HttpStatus.BAD_REQUEST);
        }

        if (userBookingDetails.getSeatNumbers().size() == 0) {
            return new ResponseEntity<Object>("seats not selected", HttpStatus.BAD_REQUEST);
        }

        MyUser user = optionalUser.get();
        Bus bus = optionalBus.get();

        setAvaliabilty(busId, userBookingDetails.getSeatNumbers());

        int bookingCharges = busService.calculateBusCharges(bus);
        Ticket ticket = new Ticket();
        
        ticket.setBus(bus);
        ticket.setPassenger(listPassengers);
        ticket.setSeatNumbers(userBookingDetails.getSeatNumbers());
        ticket.setBusCharges(bookingCharges);
        Ticket ticketSave = ticketReposistory.save(ticket);

        Orders userOrders = new Orders();
        userOrders.setTicket(ticketSave);
        userOrders.setUser(user);
        orderReposistory.save(userOrders);

        return new ResponseEntity<Object>(ticketSave, HttpStatus.OK);

    }

    private void setAvaliabilty(Long busId, ArrayList<Integer> seatNumbersList) {
        Iterable<Seat> busSeatsCollections = seatRepository.findByBusId(busId);
        Iterator<Seat> seatIterator = busSeatsCollections.iterator();

        while (seatIterator.hasNext()) {
            Seat busSeat = seatIterator.next();

            for (int index = 0; index < seatNumbersList.size(); index++){
                if(busSeat.getSeatNo() == seatNumbersList.get(index)){
                    busSeat.setIsAvaliable(false);
                    seatRepository.save(busSeat);
                    break;
                }
            }

        }

    }

}
