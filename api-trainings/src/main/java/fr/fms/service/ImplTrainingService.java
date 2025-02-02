package fr.fms.service;

import fr.fms.dao.CategoryRepository;
import fr.fms.dao.RoleRepository;
import fr.fms.dao.TrainingRepository;
import fr.fms.dao.UserRepository;
import fr.fms.entities.Category;
import fr.fms.entities.Role;
import fr.fms.entities.Training;
import fr.fms.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImplTrainingService implements ITrainingService {

    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ImplTrainingService() {
    }


    /*
     *Retrieves a list of all trainings sessions
     *@return a List of Training object
     */
    @Override
    public List<Training> getTrainings() {
        List<Training> trainings = trainingRepository.findAll();
        trainings.sort(Comparator.comparing(Training::getId));
        return trainings;

    }

    @Override
    public void deleteTrainings(Long id) {
        trainingRepository.deleteById(id);
    }

    @Override
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }


    @Override
    public Optional<Training> readTraining(Long id) {
        return trainingRepository.findById(id);
    }

    @Override
    public Training updateTraining(Long id, Training traininDetail) {
        Training training = trainingRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));

        training.setName(traininDetail.getName());
        training.setDescription(traininDetail.getDescription());
        training.setPrice(traininDetail.getPrice());
        training.setQuantity(traininDetail.getQuantity());
        if (traininDetail.getCategory() != null) {
            training.setCategory(traininDetail.getCategory());
        } else {
            throw new RuntimeException("category cannot be null");
        }


        return trainingRepository.save(training);
    }


    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }


}
