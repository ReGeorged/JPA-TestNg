package r.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "users", schema = "public", catalog = "postgres")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UsersEntity {

    @Id
    private Long id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "email")
    private String email;

}