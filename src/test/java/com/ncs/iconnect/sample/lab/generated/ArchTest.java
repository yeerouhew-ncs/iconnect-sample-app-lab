package com.ncs.iconnect.sample.lab.generated;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.ncs.iconnect.sample.lab.generated");

        noClasses()
            .that()
                .resideInAnyPackage("com.ncs.iconnect.sample.lab.generated.service..")
            .or()
                .resideInAnyPackage("com.ncs.iconnect.sample.lab.generated.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.ncs.iconnect.sample.lab.generated.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
