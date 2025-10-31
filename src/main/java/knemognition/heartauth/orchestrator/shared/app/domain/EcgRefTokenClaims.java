package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Jwt payload with ecgTest data.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class EcgRefTokenClaims {

  @Valid
  private List<List<Float>> refEcg = new ArrayList<>();

  public EcgRefTokenClaims() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EcgRefTokenClaims(List<List<Float>> refEcg) {
    this.refEcg = refEcg;
  }

  public EcgRefTokenClaims refEcg(List<List<Float>> refEcg) {
    this.refEcg = refEcg;
    return this;
  }

  public EcgRefTokenClaims addRefEcgItem(List<Float> refEcgItem) {
    if (this.refEcg == null) {
      this.refEcg = new ArrayList<>();
    }
    this.refEcg.add(refEcgItem);
    return this;
  }

  /**
   * Get refEcg
   * @return refEcg
   */
  @NotNull @Valid 
  @JsonProperty("refEcg")
  public List<List<Float>> getRefEcg() {
    return refEcg;
  }

  public void setRefEcg(List<List<Float>> refEcg) {
    this.refEcg = refEcg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EcgRefTokenClaims ecgRefTokenClaims = (EcgRefTokenClaims) o;
    return Objects.equals(this.refEcg, ecgRefTokenClaims.refEcg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(refEcg);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EcgRefTokenClaims {\n");
    sb.append("    refEcg: ").append(toIndentedString(refEcg)).append("\n");
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

    private EcgRefTokenClaims instance;

    public Builder() {
      this(new EcgRefTokenClaims());
    }

    protected Builder(EcgRefTokenClaims instance) {
      this.instance = instance;
    }

    protected Builder copyOf(EcgRefTokenClaims value) { 
      this.instance.setRefEcg(value.refEcg);
      return this;
    }

    public Builder refEcg(List<List<Float>> refEcg) {
      this.instance.refEcg(refEcg);
      return this;
    }
    
    /**
    * returns a built EcgRefTokenClaims instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public EcgRefTokenClaims build() {
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

