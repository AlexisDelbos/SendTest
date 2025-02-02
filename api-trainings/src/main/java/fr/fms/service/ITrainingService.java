package fr.fms.service;

import fr.fms.entities.Category;
import fr.fms.entities.Training;
import fr.fms.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ITrainingService {
    

    /*
     *Retrieves a list of all trainings sessions
     *@return a List of Training object
     */
    List<Training> getTrainings();

    /*
     *Delete a training sessions by its ID
     *@param id the ID of the training to delete
     */
    void deleteTrainings(Long id);

    /*
     *Save a new training or updates an existing one.
     *@param training the Training object to save
     *@return the saved Training object
     */
    Training saveTraining(Training training);

    /*
     *Retrieves a training by its unique ID
     *@param id the ID of the training to retrieve
     *@return an Optional containing the Training object if found, otherwise an empty Optional
     */
    Optional<Training> readTraining(Long id);

    /*
     *Retrieves a user by their email address
     *@param email the email of the user to retrieve
     *@return  An Optional containing the User object if found, otherwise an empty Optional
     */
//    Optional<User> readUser(String email);

    /*
     * Update an existing training with new data
     * @param id the ID of the training data to update.
     * @param training the new Training data to update with.
     * @return the updated Training object.
     * @throws Exception if there is an error during the update process
     */
    public Training updateTraining(Long id, Training training) throws Exception;

    /*

     *Retrieves a category by its unique ID
     *@param id the ID of the category to retrieve.
     *@return an Optional containing the category object if found, otherwise an empty Optional

     */
    Optional<Category> getCategoryById(Long id);

    /*
     *Retrieves all available categories
     *@return a List of category object
     */
    List<Category> getAllCategories();


}
