package knemognition.heartauth.orchestrator.challenges.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import knemognition.heartauth.orchestrator.shared.app.domain.MessageType;

import java.util.Objects;
import java.util.UUID;

/**
 * Data fields in push message.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class ChallengePushMessage {

  private UUID challengeId;

  private MessageType type;

  private String publicKey;

  private String nonce;

  private Long ttl;

  private Long exp;

  public ChallengePushMessage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChallengePushMessage(UUID challengeId, MessageType type, String publicKey, String nonce, Long ttl, Long exp) {
    this.challengeId = challengeId;
    this.type = type;
    this.publicKey = publicKey;
    this.nonce = nonce;
    this.ttl = ttl;
    this.exp = exp;
  }

  public ChallengePushMessage challengeId(UUID challengeId) {
    this.challengeId = challengeId;
    return this;
  }

  /**
   * Challenge identifier.
   * @return challengeId
   */
  @NotNull @Valid 
  @JsonProperty("challengeId")
  public UUID getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(UUID challengeId) {
    this.challengeId = challengeId;
  }

  public ChallengePushMessage type(MessageType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull @Valid 
  @JsonProperty("type")
  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public ChallengePushMessage publicKey(String publicKey) {
    this.publicKey = publicKey;
    return this;
  }

  /**
   * Device public key (PEM or JWK).
   * @return publicKey
   */
  @NotNull 
  @JsonProperty("publicKey")
  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public ChallengePushMessage nonce(String nonce) {
    this.nonce = nonce;
    return this;
  }

  /**
   * jti
   * @return nonce
   */
  @NotNull 
  @JsonProperty("nonce")
  public String getNonce() {
    return nonce;
  }

  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  public ChallengePushMessage ttl(Long ttl) {
    this.ttl = ttl;
    return this;
  }

  /**
   * Expiration epochSeconds for the pairing in seconds.
   * minimum: 1
   * @return ttl
   */
  @NotNull @Min(1L) 
  @JsonProperty("ttl")
  public Long getTtl() {
    return ttl;
  }

  public void setTtl(Long ttl) {
    this.ttl = ttl;
  }

  public ChallengePushMessage exp(Long exp) {
    this.exp = exp;
    return this;
  }

  /**
   * Expiration epochSeconds for the pairing in seconds.
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengePushMessage challengePushMessage = (ChallengePushMessage) o;
    return Objects.equals(this.challengeId, challengePushMessage.challengeId) &&
        Objects.equals(this.type, challengePushMessage.type) &&
        Objects.equals(this.publicKey, challengePushMessage.publicKey) &&
        Objects.equals(this.nonce, challengePushMessage.nonce) &&
        Objects.equals(this.ttl, challengePushMessage.ttl) &&
        Objects.equals(this.exp, challengePushMessage.exp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challengeId, type, publicKey, nonce, ttl, exp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengePushMessage {\n");
    sb.append("    challengeId: ").append(toIndentedString(challengeId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    publicKey: ").append(toIndentedString(publicKey)).append("\n");
    sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
    sb.append("    ttl: ").append(toIndentedString(ttl)).append("\n");
    sb.append("    exp: ").append(toIndentedString(exp)).append("\n");
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

    private ChallengePushMessage instance;

    public Builder() {
      this(new ChallengePushMessage());
    }

    protected Builder(ChallengePushMessage instance) {
      this.instance = instance;
    }

    protected Builder copyOf(ChallengePushMessage value) { 
      this.instance.setChallengeId(value.challengeId);
      this.instance.setType(value.type);
      this.instance.setPublicKey(value.publicKey);
      this.instance.setNonce(value.nonce);
      this.instance.setTtl(value.ttl);
      this.instance.setExp(value.exp);
      return this;
    }

    public Builder challengeId(UUID challengeId) {
      this.instance.challengeId(challengeId);
      return this;
    }
    
    public Builder type(MessageType type) {
      this.instance.type(type);
      return this;
    }
    
    public Builder publicKey(String publicKey) {
      this.instance.publicKey(publicKey);
      return this;
    }
    
    public Builder nonce(String nonce) {
      this.instance.nonce(nonce);
      return this;
    }
    
    public Builder ttl(Long ttl) {
      this.instance.ttl(ttl);
      return this;
    }
    
    public Builder exp(Long exp) {
      this.instance.exp(exp);
      return this;
    }
    
    /**
    * returns a built ChallengePushMessage instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public ChallengePushMessage build() {
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

