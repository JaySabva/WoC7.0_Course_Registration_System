package org.jaysabva.woc_crs.service.Implementation;

import org.jaysabva.woc_crs.repository.AdminRepository;
import org.jaysabva.woc_crs.repository.ProfessorRepository;
import org.jaysabva.woc_crs.repository.StudentRepository;
import org.jaysabva.woc_crs.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public AuthServiceImplementation(StudentRepository studentRepository, ProfessorRepository professorRepository, AdminRepository adminRepository) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public Object login(String email, String password) {
        try {
            Object user = studentRepository.findByEmail(email);
            if (user == null) {
                user = professorRepository.findByEmail(email);
                if (user == null) {
                    user = adminRepository.findByEmail(email);
                }
            }

            return user;
        } catch (Exception e) {
            return null;
        }
    }
}
