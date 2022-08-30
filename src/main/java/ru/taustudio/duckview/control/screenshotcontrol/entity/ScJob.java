package ru.taustudio.duckview.control.screenshotcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.BROWSER;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.OS;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.TaskStatus;

/**
 * A DVJob.
 */
@Entity
@Table(name = "sc_job")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "create_time")
    private Instant createTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_system")
    private OS operationSystem;

    @Enumerated(EnumType.STRING)
    @Column(name = "browser")
    private BROWSER browser;

    @Column(name = "url")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;


    @Column(name = "status_description")
    private String statusDescription;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private ScTask task;

    public Long getId() {
        return this.id;
    }
}
