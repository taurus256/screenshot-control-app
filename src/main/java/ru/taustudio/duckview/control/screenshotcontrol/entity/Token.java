package ru.taustudio.duckview.control.screenshotcontrol.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name="token")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder
@AllArgsConstructor
public class Token {

  public Token() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Long id;

  @Column(name="uuid")
  private String uuid;

  @Column(name="createtime")
  private LocalDateTime createTime;

  @OneToOne(targetEntity = ScUser.class)
  private ScUser user;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public ScUser getUser() {
    return user;
  }

  public void setUser(ScUser user) {
    this.user = user;
  }
}
