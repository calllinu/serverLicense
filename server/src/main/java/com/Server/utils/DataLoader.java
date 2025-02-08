package com.Server.utils;

import com.Server.repository.entity.*;
import com.Server.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import org.springframework.core.env.Environment;

@Component
public class DataLoader implements CommandLineRunner {

    private final OrganizationRepository organizationRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final RegistrationRequestRepository registrationRequestRepository;
    private final Environment environment;

    public DataLoader(OrganizationRepository organizationRepository,
                      SubsidiaryRepository subsidiaryRepository,
                      EmployeeRepository employeeRepository,
                      UserRepository userRepository,
                      RegistrationRequestRepository registrationRequestRepository,
                      Environment environment) {
        this.organizationRepository = organizationRepository;
        this.subsidiaryRepository = subsidiaryRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.registrationRequestRepository = registrationRequestRepository;
        this.environment = environment;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Check if the ddl-auto property is set to create
        String ddlAuto = environment.getProperty("spring.jpa.hibernate.ddl-auto");
        if ("create".equalsIgnoreCase(ddlAuto)) {
            loadData();
        }
    }

    private void loadData() {
        // Your existing logic for loading data
        Random random = new Random();

        // Realistic organization names
        List<String> organizationNames = List.of(
                "EcoTech Solutions", "Green Industries", "EuroBuild Group", "AgriCore Holdings",
                "Urban Logistics Ltd.", "BlueSky Manufacturing", "Nordic Ventures", "PoliMetal Ltd.",
                "SmartBuild Europe", "TerraNova Group", "FutureFoods Inc.", "AquaTech Solutions",
                "SolarWave Systems", "AlphaDrive Logistics", "ProAgriTech", "SteelEdge Engineering",
                "Zenith Construction", "Visionary Designs", "BioHarvest Labs", "PrimeWare Ltd.",
                "NextGen Textiles", "TechSphere Innovations", "Riverstone Logistics", "Horizon AgriTech",
                "SteelCore International", "UrbanNest Developments", "EcoSmart Holdings", "CivicBuild Systems",
                "HarvestLine Inc.", "PeakVision Manufacturing", "NovaLogix Transport", "FuturePath Inc.",
                "Aurora Engineering", "BlueHarvest Agri", "LandBridge Logistics", "Skyline Innovations",
                "BrightPeak Systems", "GreenFuture Technologies", "ClearWave Solutions", "PrimeCore Europe",
                "OceanEdge Ltd.", "PioneerBuild Group", "SolarEdge Technologies", "Global Horizons Inc.",
                "BioNova International", "DynamicEdge Industries", "GreenSphere Logistics", "PureTech Labs",
                "EuroRise Ventures", "OptimaBuild Systems", "VistaWave Technologies"
        );

        // Realistic names for people, countries, cities, and streets
        List<String> firstNames = List.of("Liam", "Olivia", "Noah", "Emma", "Oliver", "Ava", "Sophia", "Mia", "Lucas", "Charlotte");
        List<String> lastNames = List.of("Smith", "Brown", "Johnson", "Garcia", "Martinez", "Davis", "Miller", "Wilson", "Anderson", "Taylor");
        List<String> countries = List.of("Germany", "France", "Spain", "Italy", "Netherlands", "Sweden", "Poland");
        List<String> cities = List.of("Berlin", "Paris", "Madrid", "Rome", "Amsterdam", "Stockholm", "Warsaw");
        List<String> streets = List.of("Main Street", "Oak Avenue", "Pine Road", "Elm Street", "Maple Drive", "Sunset Boulevard", "Broadway");

        final boolean[] ownerAssigned = {false}; // Ensures only one owner in the app
        Set<String> usedEmails = new HashSet<>(); // Track already used emails globally

        IntStream.range(0, 50).forEach(orgIndex -> {
            // Create and save an Organization
            Organization organization = new Organization();
            organization.setOrganizationCode("ORG" + (orgIndex + 1));
            organization.setName(organizationNames.get(orgIndex)); // Use realistic organization names
            organization.setYearOfEstablishment(1990L + random.nextInt(30));
            organization.setIndustry(Industry.values()[random.nextInt(Industry.values().length)]);

            List<Subsidiary> subsidiaries = new ArrayList<>();

            IntStream.range(1, random.nextInt(3) + 5).forEach(subIndex -> {
                // Create and save a Subsidiary
                Subsidiary subsidiary = new Subsidiary();
                subsidiary.setSubsidiaryCode("SUBS" + (orgIndex + 1) + "-" + subIndex);
                subsidiary.setCountry(countries.get(random.nextInt(countries.size())));
                subsidiary.setCity(cities.get(random.nextInt(cities.size())));
                subsidiary.setAddress(streets.get(random.nextInt(streets.size())) + " No. " + (10 + random.nextInt(90)));
                subsidiary.setOrganization(organization);

                List<Employee> employees = new ArrayList<>();
                Set<String> usedNames = new HashSet<>();

                IntStream.range(1, 16).forEach(empIndex -> {
                    String firstName;
                    String lastName;

                    // Ensure unique names per subsidiary
                    do {
                        firstName = firstNames.get(random.nextInt(firstNames.size()));
                        lastName = lastNames.get(random.nextInt(lastNames.size()));
                    } while (usedNames.contains(firstName + " " + lastName));
                    usedNames.add(firstName + " " + lastName);

                    String fullName = firstName + " " + lastName;

                    // Create a base email
                    String baseEmail = fullName.replace(" ", ".").toLowerCase() + "@example.com";
                    String email = baseEmail;

                    // Ensure email uniqueness
                    int emailCounter = 1;
                    while (usedEmails.contains(email)) {
                        email = baseEmail.replace("@example.com", "") + emailCounter + "@example.com";
                        emailCounter++;
                    }
                    usedEmails.add(email);

                    // Create and save an Employee
                    Employee employee = new Employee();
                    employee.setFullName(fullName);
                    employee.setEmployeeCNP("CNP" + (orgIndex + 1) + subIndex + empIndex);
                    LocalDate dateOfBirth = LocalDate.of(1980 + random.nextInt(30), 1 + random.nextInt(12), 1 + random.nextInt(28));
                    employee.setDateOfBirth(dateOfBirth);

                    employee.setQualification(Qualification.values()[random.nextInt(Qualification.values().length)]);
                    employee.setYearsOfExperience(random.nextInt(20));
                    LocalDate dateOfHiring = LocalDate.of(2000 + random.nextInt(23), 1 + random.nextInt(12), 1 + random.nextInt(28));
                    employee.setDateOfHiring(dateOfHiring);
                    employee.setSubsidiary(subsidiary);

                    // Create and save a User
                    User user = new User();
                    user.setUsername(fullName.replace(" ", "").toLowerCase());
                    user.setEmail(email);
                    user.setFullName(fullName);
                    user.setPassword("password" + empIndex);

                    if (!ownerAssigned[0] && empIndex == 1 && subIndex == 1) {
                        user.setRole(Role.OWNER);
                        ownerAssigned[0] = true;
                    } else if (empIndex == 1) {
                        user.setRole(Role.ORG_ADMIN);
                    } else {
                        user.setRole(Role.EMPLOYEE);
                    }

                    userRepository.save(user);
                    employee.setUser(user);
                    employees.add(employee);
                });

                subsidiary.setEmployees(employees);
                employees.forEach(employeeRepository::save);
                subsidiaries.add(subsidiary);
                subsidiaryRepository.save(subsidiary);
            });

            organization.setSubsidiaries(subsidiaries);
            organizationRepository.save(organization);

            IntStream.range(1, random.nextInt(3) + 3).forEach(reqIndex -> {
                RegistrationRequest request = new RegistrationRequest();
                request.setUsername("requestUser" + (orgIndex + 1) + reqIndex);
                request.setEmail("requestUser" + (orgIndex + 1) + reqIndex + "@example.com");
                request.setFullName("Request User " + (orgIndex + 1) + reqIndex);
                request.setPassword("password" + reqIndex);
                request.setAdminId(organization.getSubsidiaries().get(0).getEmployees().get(0).getEmployeeId());
                request.setSubsidiary(organization.getSubsidiaries().get(0));
                request.setStatus(RequestStatus.PENDING);

                registrationRequestRepository.save(request);
            });
        });

        System.out.println("Data loaded successfully!");
    }
}
