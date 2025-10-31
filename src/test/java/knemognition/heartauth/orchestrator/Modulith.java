package knemognition.heartauth.orchestrator;



import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithDocumentationTest {

    @Test
    void writeDocumentationSnippets() {
        // Build from your base package (or pass your Boot main class’ package)
        ApplicationModules modules = ApplicationModules.of("knemognition.heartauth.orchestrator");

        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml(); // generates .puml/.adoc
    }
}
