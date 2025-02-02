package fr.fms;

import fr.fms.dao.CategoryRepository;
import fr.fms.dao.RoleRepository;
import fr.fms.dao.TrainingRepository;
import fr.fms.dao.UserRepository;
import fr.fms.entities.Category;
import fr.fms.entities.Role;
import fr.fms.entities.Training;
import fr.fms.entities.User;
import fr.fms.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class ApiTrainingsApplication implements CommandLineRunner {

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    AccountServiceImpl accountService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {

        SpringApplication.run(ApiTrainingsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        dataTrainings();
//        dataUsers();
    }


    private void dataTrainings() {
        Category cms = categoryRepository.save(new Category(null, "CMS", null));
        Category dev = categoryRepository.save(new Category(null, "Langage Dev", null));
        Category framework = categoryRepository.save(new Category(null, "Framework", null));

        trainingRepository.save(new Training(null, "Java", "PEEPO Java Standard Edition 8 sur 5 Jours", 3500.0, 1, dev));
        trainingRepository.save(new Training(null, "DotNet", "DotNet  & entityframework en 5 jours", 2750.0, 1, dev));
        trainingRepository.save(new Training(null, "PowerBi", "Business Intelligence 5 jours", 3000.0, 1, dev));
        trainingRepository.save(new Training(null, "NodeJs", "Prise en main de NodeJs/Express 2 jours", 1400.0, 1, dev));
        trainingRepository.save(new Training(null, "Php", "Initiation au Dev/Web avec hp 4 jours", 1300.0, 1, dev));
        trainingRepository.save(new Training(null, "Python", "Python sur 5 jours", 2780.0, 1, dev));

        trainingRepository.save(new Training(null, "WordPress", "Cree un site Web en 20 minutes", 3500.0, 1, cms));
        trainingRepository.save(new Training(null, "Joomla", "Tout sur Joomla en  5 jours", 2478, 1, cms));
        trainingRepository.save(new Training(null, "Dupral", "Comprendre Duprla en 2 jours ", 3000.0, 1, cms));
        trainingRepository.save(new Training(null, "PresstaShop", "Prise en main en 10 jours ", 1400.0, 1, cms));
        trainingRepository.save(new Training(null, "Magento", "Initiation en  4 jours", 1300.0, 1, cms));

        trainingRepository.save(new Training(null, "React", "Maitriser React ", 2569, 1, framework));
        trainingRepository.save(new Training(null, "Angular", "Tout sur Angular en  15 jours", 2478, 1, framework));
        trainingRepository.save(new Training(null, "Django", "Comprendre tout sur django", 3000.0, 1, framework));
        trainingRepository.save(new Training(null, "Ruby", "Prise en main en 10 jours ", 1400.0, 1, framework));
        trainingRepository.save(new Training(null, ".NET", "Initiation en  16 jours", 1300.0, 1, framework));

    }

    private void dataUsers() {
        String HashPassword = passwordEncoder.encode("12345");

        User user = new User(null, "mohamed@mail.com", HashPassword, new ArrayList<>());
        User user2 = new User(null, "aymene@mail.com", HashPassword, new ArrayList<>());

        accountService.saveUser(user);
        accountService.saveUser(user2);

        Role role = new Role(null, "ROLE_USER", null);
        Role role2 = new Role(null, "ROLE_ADMIN", null);
        accountService.saveRole(role2);
        accountService.saveRole(role);

        user.getRoles().add(role);
        user.getRoles().add(role2);
        user2.getRoles().add(role);

        accountService.saveUser(user);
        accountService.saveUser(user2);
    }

}
