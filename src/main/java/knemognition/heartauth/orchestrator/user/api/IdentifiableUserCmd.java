package knemognition.heartauth.orchestrator.user.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * Shared identity of a user within a tenant.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class IdentifiableUserCmd {

  private UUID userId;

  private UUID tenantId;

  public IdentifiableUserCmd() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public IdentifiableUserCmd(UUID userId, UUID tenantId) {
    this.userId = userId;
    this.tenantId = tenantId;
  }

  public IdentifiableUserCmd userId(UUID userId) {
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

  public IdentifiableUserCmd tenantId(UUID tenantId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentifiableUserCmd identifiableUser = (IdentifiableUserCmd) o;
    return Objects.equals(this.userId, identifiableUser.userId) &&
        Objects.equals(this.tenantId, identifiableUser.tenantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tenantId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentifiableUser {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
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

    private IdentifiableUserCmd instance;

    public Builder() {
      this(new IdentifiableUserCmd());
    }

    protected Builder(IdentifiableUserCmd instance) {
      this.instance = instance;
    }

    protected Builder copyOf(IdentifiableUserCmd value) {
      this.instance.setUserId(value.userId);
      this.instance.setTenantId(value.tenantId);
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
    
    /**
    * returns a built IdentifiableUser instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public IdentifiableUserCmd build() {
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

