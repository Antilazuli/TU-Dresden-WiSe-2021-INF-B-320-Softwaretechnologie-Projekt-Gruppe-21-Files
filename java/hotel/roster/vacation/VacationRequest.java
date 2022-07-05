package hotel.roster.vacation;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.core.SalespointIdentifier;

import hotel.member.staff.Staff;
import hotel.roster.vacation.VacationEvents.AcceptVacation;

@Entity
public class VacationRequest extends AbstractAggregateRoot<SalespointIdentifier> {

    private @Id SalespointIdentifier id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Staff staff;

    private LocalDate start;
    private LocalDate end;

    private String comment;

    private State state;

    public enum State {
        OPEN {
            @Override
            public String toString() {
                return "offen";
            }
        },
        ACCEPTED {
            @Override
            public String toString() {
                return "genemigt";
            }
        },
        DECLINED {
            @Override
            public String toString() {
                return "abgelehnt";
            }
        };
    }

    public VacationRequest() {
    }

    public VacationRequest(Staff staff, LocalDate start, LocalDate end, String comment) {

        this.staff = staff;
        this.start = start;
        this.end = end;
        this.comment = comment;

        id = new SalespointIdentifier();
        state = State.OPEN;
    }

    public SalespointIdentifier getId() {
        return id;
    }

    public Staff getStaff() {
        return staff;
    }

    public State getState() {
        return state;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalDate[] getDateSpan() {
        return start.datesUntil(end.plusDays(1)).toArray(LocalDate[]::new);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean accept() {

        if (isOpen()) {
            state = State.ACCEPTED;

            registerEvent(AcceptVacation.of(this));

            return true;
        }

        return false;
    }

    public boolean decline() {

        if (isOpen()) {

            state = State.DECLINED;

            return true;
        }

        return false;
    }

    public boolean isOpen() {
        return state == State.OPEN;
    }
}
