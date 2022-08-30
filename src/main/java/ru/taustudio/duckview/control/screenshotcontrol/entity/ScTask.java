package ru.taustudio.duckview.control.screenshotcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.BROWSER;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.OS;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.Resolution;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.TaskStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * A DVTask.
 */
@Entity
@Table(name = "sc_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode
public class ScTask implements Serializable {

    public ScTask(){
        winFirefox = false;
        winChrome = false;
        winOpera = false;
        winEdge = false;
        linFirefox = false;
        linChrome = false;
        linOpera = false;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "create_time")
    private Instant createTime;

    @NotNull(message = "URL must be set")
    @Pattern(regexp="^.*\\..*")
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution")
    private Resolution resolution;

    private Boolean winFirefox;
    private Boolean winChrome;
    private Boolean winOpera;
    private Boolean winEdge;
    private Boolean linFirefox;
    private Boolean linChrome;
    private Boolean linOpera;

    @OneToMany(mappedBy = "task")
    List<ScJob> jobList;

    @ManyToOne
    @JsonIgnore
    private ScUser user;
}
