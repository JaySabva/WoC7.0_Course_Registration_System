package org.jaysabva.woc_crs.service.Implementation;

import org.jaysabva.woc_crs.entity.Admin;
import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.entity.Student;
import org.jaysabva.woc_crs.repository.AdminRepository;
import org.jaysabva.woc_crs.repository.ProfessorRepository;
import org.jaysabva.woc_crs.repository.StudentRepository;
import org.jaysabva.woc_crs.service.AuthService;
import org.jaysabva.woc_crs.util.BCryptUtil;
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

            if (user == null) {
                return null;
            }

            String storedPassword = null;

            if (user instanceof Student) {
                storedPassword = ((Student) user).getPassword();
            } else if (user instanceof Professor) {
                storedPassword = ((Professor) user).getPassword();
            } else if (user instanceof Admin) {
                storedPassword = ((Admin) user).getPassword();
            }

            if (storedPassword != null && BCryptUtil.checkPassword(password, storedPassword)) {
                return user;
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }
}
