package ru.taustudio.duckview.control.screenshotcontrol.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.taustudio.duckview.control.screenshotcontrol.entity.enumeration.Resolution;

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
        iosIPHONE_SE=false;
        iosIPHONE_PRO = false;
        iosIPAD = false;
        macSafari = false;
        macFirefox = false;
        createTime = Instant.now();
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
    private Boolean iosIPAD;
    private Boolean iosIPHONE_SE;
    private Boolean iosIPHONE_PRO;
    private Boolean macSafari;
    private Boolean macFirefox;

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<ScJob> jobList;

    @ManyToOne
    @JsonIgnore
    private ScUser user;
}
