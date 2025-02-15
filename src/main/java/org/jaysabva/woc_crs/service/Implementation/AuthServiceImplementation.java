package org.jaysabva.woc_crs.service.Implementation;

import org.jaysabva.woc_crs.entity.Admin;
import org.jaysabva.woc_crs.entity.Professor;
import org.jaysabva.woc_crs.entity.Student;
import org.jaysabva.woc_crs.repository.AdminRepository;
import org.jaysabva.woc_crs.repository.ProfessorRepository;
import org.jaysabva.woc_crs.repository.StudentRepository;
import org.jaysabva.woc_crs.service.AuthService;
import org.jaysabva.woc_crs.util.BCryptUtil;
import org.jaysabva.woc_crs.util.EmailSenderService;
import org.jaysabva.woc_crs.util.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {

    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final AdminRepository adminRepository;
    private final JwtProvider jwtProvider;
    private final EmailSenderService emailSenderService;

    @Autowired
    public AuthServiceImplementation(StudentRepository studentRepository, ProfessorRepository professorRepository, AdminRepository adminRepository, JwtProvider jwtProvider, EmailSenderService emailSenderService) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.adminRepository = adminRepository;
        this.jwtProvider = jwtProvider;
        this.emailSenderService = emailSenderService;
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

    @Override
    public String forgotPassword(String email) {
        try {
            if (studentRepository.existsByEmail(email) || professorRepository.existsByEmail(email) || adminRepository.existsByEmail(email)) {
                String token = jwtProvider.passwordResetToken(email);
                String resetURL = "http://localhost:3000/reset-password?token=" + token;
                emailSenderService.sendEmail(email, "Password Reset Link", emailSenderService.forgotPasswordEmail(resetURL));

                return "Password reset link sent to your email";
            }

            return "User with this email does not exist";
        } catch (Exception e) {
            return "An error occurred during password reset";
        }
    }

    @Override
    public String resetPassword(String email, String password) {
        try {
            if (studentRepository.existsByEmail(email)) {
                Student student = studentRepository.findByEmail(email);
                student.setPassword(BCryptUtil.hashPassword(password));
                studentRepository.save(student);
            } else if (professorRepository.existsByEmail(email)) {
                Professor professor = professorRepository.findByEmail(email);
                professor.setPassword(BCryptUtil.hashPassword(password));
                professorRepository.save(professor);
            } else if (adminRepository.existsByEmail(email)) {
                Admin admin = adminRepository.findByEmail(email);
                admin.setPassword(BCryptUtil.hashPassword(password));
                adminRepository.save(admin);
            } else {
                return "User with this email does not exist";
            }

            return "Password reset successful";
        } catch (Exception e) {
            return "An error occurred during password reset";
        }
    }
}
