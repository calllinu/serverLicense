//package com.Server.service.implementation;
//
//import com.Server.repository.OrganizationRepository;
//import com.Server.repository.RegistrationRequestRepository;
//import com.Server.repository.entity.Organization;
//import com.Server.repository.entity.RegistrationRequest;
//import com.Server.service.RequestService;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class RequestServiceImpl implements RequestService {
//    private final RegistrationRequestRepository registrationRequestRepository;
//    private final OrganizationRepository organizationRepository;
//
//    public RequestServiceImpl(RegistrationRequestRepository registrationRequestRepository, OrganizationRepository organizationRepository) {
//        this.registrationRequestRepository = registrationRequestRepository;
//        this.organizationRepository = organizationRepository;
//    }
//
////    @Override
////    public List<RegistrationRequest> getAllRequestsByAdminId(Long adminId) {
////        List<Organization> organizations = organizationRepository.findByAdminId(adminId);
////
////        if (organizations.isEmpty()) {
////            throw new IllegalArgumentException("No organizations found for the given admin ID");
////        }
////
////        return organizations.stream()
////                .flatMap(org -> org.getSubsidiaries().stream())
////                .flatMap(sub -> registrationRequestRepository.findBySubsidiaryId(sub.getSubsidiaryId()).stream())
////                .collect(Collectors.toList());
////    }
//}
//
