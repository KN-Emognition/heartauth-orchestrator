package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * QrCodeClaims
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class QrCodeClaims {

  private UUID userId;

  private UUID tenantId;

  private Long exp;

  private UUID jti;

  public QrCodeClaims() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public QrCodeClaims(UUID userId, UUID tenantId, Long exp, UUID jti) {
    this.userId = userId;
    this.tenantId = tenantId;
    this.exp = exp;
    this.jti = jti;
  }

  public QrCodeClaims userId(UUID userId) {
    this.userId = userId;
    return this;
  }

  /**
   * User identifier.
   * @return userId
   */
  @NotNull @Valid 
  @JsonProperty("userId")
  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public QrCodeClaims tenantId(UUID tenantId) {
    this.tenantId = tenantId;
    return this;
  }

  /**
   * Tenant identifier.
   * @return tenantId
   */
  @NotNull @Valid 
  @JsonProperty("tenantId")
  public UUID getTenantId() {
    return tenantId;
  }

  public void setTenantId(UUID tenantId) {
    this.tenantId = tenantId;
  }

  public QrCodeClaims exp(Long exp) {
    this.exp = exp;
    return this;
  }

  /**
   * Time-to-live for the pairing in seconds.
   * minimum: 1
   * @return exp
   */
  @NotNull @Min(1L) 
  @JsonProperty("exp")
  public Long getExp() {
    return exp;
  }

  public void setExp(Long exp) {
    this.exp = exp;
  }

  public QrCodeClaims jti(UUID jti) {
    this.jti = jti;
    return this;
  }

  /**
   * jti
   * @return jti
   */
  @NotNull @Valid 
  @JsonProperty("jti")
  public UUID getJti() {
    return jti;
  }

  public void setJti(UUID jti) {
    this.jti = jti;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QrCodeClaims qrCodeClaims = (QrCodeClaims) o;
    return Objects.equals(this.userId, qrCodeClaims.userId) &&
        Objects.equals(this.tenantId, qrCodeClaims.tenantId) &&
        Objects.equals(this.exp, qrCodeClaims.exp) &&
        Objects.equals(this.jti, qrCodeClaims.jti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tenantId, exp, jti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QrCodeClaims {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
    sb.append("    jti: ").append(toIndentedString(jti)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
  public static class Builder {

    private QrCodeClaims instance;

    public Builder() {
      this(new QrCodeClaims());
    }

    protected Builder(QrCodeClaims instance) {
      this.instance = instance;
    }

    protected Builder copyOf(QrCodeClaims value) { 
      this.instance.setUserId(value.userId);
      this.instance.setTenantId(value.tenantId);
      this.instance.setExp(value.exp);
      this.instance.setJti(value.jti);
      return this;
    }

    public Builder userId(UUID userId) {
      this.instance.userId(userId);
      return this;
    }
    
    public Builder tenantId(UUID tenantId) {
      this.instance.tenantId(tenantId);
      return this;
    }
    
    public Builder exp(Long exp) {
      this.instance.exp(exp);
      return this;
    }
    
    public Builder jti(UUID jti) {
      this.instance.jti(jti);
      return this;
    }
    
    /**
    * returns a built QrCodeClaims instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public QrCodeClaims build() {
      try {
        return this.instance;
      } finally {
        // ensure that this.instance is not reused
        this.instance = null;
      }
    }

    @Override
    public String toString() {
      return getClass() + "=(" + instance + ")";
    }
  }

  /**
  * Create a builder with no initialized field (except for the default values).
  */
  public static Builder builder() {
    return new Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public Builder toBuilder() {
    Builder builder = new Builder();
    return builder.copyOf(this);
  }

}

