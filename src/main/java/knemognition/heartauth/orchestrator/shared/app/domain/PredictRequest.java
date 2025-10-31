package knemognition.heartauth.orchestrator.shared.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Flexible input payload accepted by the model.
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-10-25T08:37:27.755305314+02:00[Europe/Warsaw]", comments = "Generator version: 7.15.0")
public class PredictRequest {

  @Valid
  private List<List<Float>> refEcg = new ArrayList<>();

  @Valid
  private List<Float> testEcg = new ArrayList<>();

  public PredictRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PredictRequest(List<List<Float>> refEcg, List<Float> testEcg) {
    this.refEcg = refEcg;
    this.testEcg = testEcg;
  }

  public PredictRequest refEcg(List<List<Float>> refEcg) {
    this.refEcg = refEcg;
    return this;
  }

  public PredictRequest addRefEcgItem(List<Float> refEcgItem) {
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

  public PredictRequest testEcg(List<Float> testEcg) {
    this.testEcg = testEcg;
    return this;
  }

  public PredictRequest addTestEcgItem(Float testEcgItem) {
    if (this.testEcg == null) {
      this.testEcg = new ArrayList<>();
    }
    this.testEcg.add(testEcgItem);
    return this;
  }

  /**
   * Get testEcg
   * @return testEcg
   */
  @NotNull 
  @JsonProperty("testEcg")
  public List<Float> getTestEcg() {
    return testEcg;
  }

  public void setTestEcg(List<Float> testEcg) {
    this.testEcg = testEcg;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PredictRequest predictRequest = (PredictRequest) o;
    return Objects.equals(this.refEcg, predictRequest.refEcg) &&
        Objects.equals(this.testEcg, predictRequest.testEcg);
  }

  @Override
  public int hashCode() {
    return Objects.hash(refEcg, testEcg);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PredictRequest {\n");
    sb.append("    refEcg: ").append(toIndentedString(refEcg)).append("\n");
    sb.append("    testEcg: ").append(toIndentedString(testEcg)).append("\n");
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

    private PredictRequest instance;

    public Builder() {
      this(new PredictRequest());
    }

    protected Builder(PredictRequest instance) {
      this.instance = instance;
    }

    protected Builder copyOf(PredictRequest value) { 
      this.instance.setRefEcg(value.refEcg);
      this.instance.setTestEcg(value.testEcg);
      return this;
    }

    public Builder refEcg(List<List<Float>> refEcg) {
      this.instance.refEcg(refEcg);
      return this;
    }
    
    public Builder testEcg(List<Float> testEcg) {
      this.instance.testEcg(testEcg);
      return this;
    }
    
    /**
    * returns a built PredictRequest instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public PredictRequest build() {
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

