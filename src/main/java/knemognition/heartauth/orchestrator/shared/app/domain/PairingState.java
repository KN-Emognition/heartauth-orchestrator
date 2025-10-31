package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import knemognition.heartauth.orchestrator.shared.FlowStatus;
import knemognition.heartauth.orchestrator.shared.Platform;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * PairingState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class PairingState {

  private UUID userId;

  private UUID tenantId;

  private @Nullable UUID id;

  private FlowStatus status;

  private @Nullable String reason;

  private @Nullable String deviceId;

  private @Nullable String displayName;

  private @Nullable String publicKey;

  private @Nullable String fcmToken;

  private @Nullable Platform platform;

  private @Nullable String osVersion;

  private @Nullable String model;

  private @Nullable String nonceB64;

  private @Nullable Long exp;

  private Long createdAt;

  private Long ttlSeconds;

  public PairingState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PairingState(UUID userId, UUID tenantId, FlowStatus status, Long createdAt, Long ttlSeconds) {
    this.userId = userId;
    this.tenantId = tenantId;
    this.status = status;
    this.createdAt = createdAt;
    this.ttlSeconds = ttlSeconds;
  }

  public PairingState userId(UUID userId) {
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

  public PairingState tenantId(UUID tenantId) {
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

  public PairingState id(@Nullable UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid 
  @JsonProperty("id")
  public @Nullable UUID getId() {
    return id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public PairingState status(FlowStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @JsonProperty("status")
  public FlowStatus getStatus() {
    return status;
  }

  public void setStatus(FlowStatus status) {
    this.status = status;
  }

  public PairingState reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Optional human-readable reason for non-success states.
   * @return reason
   */
  
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public PairingState deviceId(@Nullable String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  /**
   * Server-assigned or client-reported device identifier.
   * @return deviceId
   */
  
  @JsonProperty("deviceId")
  public @Nullable String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(@Nullable String deviceId) {
    this.deviceId = deviceId;
  }

  public PairingState displayName(@Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Friendly device name shown to the user.
   * @return displayName
   */
  
  @JsonProperty("displayName")
  public @Nullable String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@Nullable String displayName) {
    this.displayName = displayName;
  }

  public PairingState publicKey(@Nullable String publicKey) {
    this.publicKey = publicKey;
    return this;
  }

  /**
   * Device public key (PEM or JWK).
   * @return publicKey
   */
  
  @JsonProperty("publicKey")
  public @Nullable String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(@Nullable String publicKey) {
    this.publicKey = publicKey;
  }

  public PairingState fcmToken(@Nullable String fcmToken) {
    this.fcmToken = fcmToken;
    return this;
  }

  /**
   * Firebase Cloud Messaging token for push delivery.
   * @return fcmToken
   */
  
  @JsonProperty("fcmToken")
  public @Nullable String getFcmToken() {
    return fcmToken;
  }

  public void setFcmToken(@Nullable String fcmToken) {
    this.fcmToken = fcmToken;
  }

  public PairingState platform(@Nullable Platform platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  @Valid 
  @JsonProperty("platform")
  public @Nullable Platform getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable Platform platform) {
    this.platform = platform;
  }

  public PairingState osVersion(@Nullable String osVersion) {
    this.osVersion = osVersion;
    return this;
  }

  /**
   * OS version string reported by the device.
   * @return osVersion
   */
  
  @JsonProperty("osVersion")
  public @Nullable String getOsVersion() {
    return osVersion;
  }

  public void setOsVersion(@Nullable String osVersion) {
    this.osVersion = osVersion;
  }

  public PairingState model(@Nullable String model) {
    this.model = model;
    return this;
  }

  /**
   * Device model string.
   * @return model
   */
  
  @JsonProperty("model")
  public @Nullable String getModel() {
    return model;
  }

  public void setModel(@Nullable String model) {
    this.model = model;
  }

  public PairingState nonceB64(@Nullable String nonceB64) {
    this.nonceB64 = nonceB64;
    return this;
  }

  /**
   * Base64-encoded nonce used during pairing.
   * @return nonceB64
   */
  
  @JsonProperty("nonceB64")
  public @Nullable String getNonceB64() {
    return nonceB64;
  }

  public void setNonceB64(@Nullable String nonceB64) {
    this.nonceB64 = nonceB64;
  }

  public PairingState exp(@Nullable Long exp) {
    this.exp = exp;
    return this;
  }

  /**
   * Expiration time as UNIX epoch seconds.
   * @return exp
   */
  
  @JsonProperty("exp")
  public @Nullable Long getExp() {
    return exp;
  }

  public void setExp(@Nullable Long exp) {
    this.exp = exp;
  }

  public PairingState createdAt(Long createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Creation time as UNIX epoch seconds.
   * @return createdAt
   */
  @NotNull 
  @JsonProperty("createdAt")
  public Long getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Long createdAt) {
    this.createdAt = createdAt;
  }

  public PairingState ttlSeconds(Long ttlSeconds) {
    this.ttlSeconds = ttlSeconds;
    return this;
  }

  /**
   * Time-to-live for the pairing in seconds.
   * minimum: 1
   * @return ttlSeconds
   */
  @NotNull @Min(1L) 
  @JsonProperty("ttlSeconds")
  public Long getTtlSeconds() {
    return ttlSeconds;
  }

  public void setTtlSeconds(Long ttlSeconds) {
    this.ttlSeconds = ttlSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PairingState pairingState = (PairingState) o;
    return Objects.equals(this.userId, pairingState.userId) &&
        Objects.equals(this.tenantId, pairingState.tenantId) &&
        Objects.equals(this.id, pairingState.id) &&
        Objects.equals(this.status, pairingState.status) &&
        Objects.equals(this.reason, pairingState.reason) &&
        Objects.equals(this.deviceId, pairingState.deviceId) &&
        Objects.equals(this.displayName, pairingState.displayName) &&
        Objects.equals(this.publicKey, pairingState.publicKey) &&
        Objects.equals(this.fcmToken, pairingState.fcmToken) &&
        Objects.equals(this.platform, pairingState.platform) &&
        Objects.equals(this.osVersion, pairingState.osVersion) &&
        Objects.equals(this.model, pairingState.model) &&
        Objects.equals(this.nonceB64, pairingState.nonceB64) &&
        Objects.equals(this.exp, pairingState.exp) &&
        Objects.equals(this.createdAt, pairingState.createdAt) &&
        Objects.equals(this.ttlSeconds, pairingState.ttlSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, tenantId, id, status, reason, deviceId, displayName, publicKey, fcmToken, platform, osVersion, model, nonceB64, exp, createdAt, ttlSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PairingState {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    tenantId: ").append(toIndentedString(tenantId)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
    sb.append("    fcmToken: ").append(toIndentedString(fcmToken)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    osVersion: ").append(toIndentedString(osVersion)).append("\n");
    sb.append("    model: ").append(toIndentedString(model)).append("\n");
    sb.append("    nonceB64: ").append(toIndentedString(nonceB64)).append("\n");
    sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    ttlSeconds: ").append(toIndentedString(ttlSeconds)).append("\n");
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

    private PairingState instance;

    public Builder() {
      this(new PairingState());
    }

    protected Builder(PairingState instance) {
      this.instance = instance;
    }

    protected Builder copyOf(PairingState value) { 
      this.instance.setUserId(value.userId);
      this.instance.setTenantId(value.tenantId);
      this.instance.setId(value.id);
      this.instance.setStatus(value.status);
      this.instance.setReason(value.reason);
      this.instance.setDeviceId(value.deviceId);
      this.instance.setDisplayName(value.displayName);
      this.instance.setPublicKey(value.publicKey);
      this.instance.setFcmToken(value.fcmToken);
      this.instance.setPlatform(value.platform);
      this.instance.setOsVersion(value.osVersion);
      this.instance.setModel(value.model);
      this.instance.setNonceB64(value.nonceB64);
      this.instance.setExp(value.exp);
      this.instance.setCreatedAt(value.createdAt);
      this.instance.setTtlSeconds(value.ttlSeconds);
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
    
    public Builder id(UUID id) {
      this.instance.id(id);
      return this;
    }
    
    public Builder status(FlowStatus status) {
      this.instance.status(status);
      return this;
    }
    
    public Builder reason(String reason) {
      this.instance.reason(reason);
      return this;
    }
    
    public Builder deviceId(String deviceId) {
      this.instance.deviceId(deviceId);
      return this;
    }
    
    public Builder displayName(String displayName) {
      this.instance.displayName(displayName);
      return this;
    }
    
    public Builder publicKey(String publicKey) {
      this.instance.publicKey(publicKey);
      return this;
    }
    
    public Builder fcmToken(String fcmToken) {
      this.instance.fcmToken(fcmToken);
      return this;
    }
    
    public Builder platform(Platform platform) {
      this.instance.platform(platform);
      return this;
    }
    
    public Builder osVersion(String osVersion) {
      this.instance.osVersion(osVersion);
      return this;
    }
    
    public Builder model(String model) {
      this.instance.model(model);
      return this;
    }
    
    public Builder nonceB64(String nonceB64) {
      this.instance.nonceB64(nonceB64);
      return this;
    }
    
    public Builder exp(Long exp) {
      this.instance.exp(exp);
      return this;
    }
    
    public Builder createdAt(Long createdAt) {
      this.instance.createdAt(createdAt);
      return this;
    }
    
    public Builder ttlSeconds(Long ttlSeconds) {
      this.instance.ttlSeconds(ttlSeconds);
      return this;
    }
    
    /**
    * returns a built PairingState instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public PairingState build() {
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

