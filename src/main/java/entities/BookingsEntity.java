package entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "bookings", schema = "public", catalog = "postgres")
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookingsEntity {
    @Basic
    @Column(name = "created_date")
    private Date createdDate;
    @Basic
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;
    @Basic
    @Column(name = "record_state")
    private Short recordState;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "booking_id")
    private long bookingId;
    @Basic
    @Column(name = "end_date")
    private Timestamp endDate;
    @Basic
    @Column(name = "room_id")
    private Long roomId;
    @Basic
    @Column(name = "start_date")
    private Timestamp startDate;
    @Basic
    @Column(name = "booking_user")
    private String bookingUser;

}
