package fr.fms.web;

import fr.fms.entities.Category;
import fr.fms.entities.Training;
import fr.fms.entities.User;
import fr.fms.service.ImplTrainingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class to handle HTTP requests related to Training and category entities
 * This class provides endpoints ton create, read update and delete trainings,
 * manage users, and retrieve categories
 *
 * @author [IbosDev]
 * @version 1.0
 * @since 2025-01-28
 */

@CrossOrigin("*")
@RestController
@Transactional
@RequestMapping("/api")
public class TrainingController {

    @Autowired
    private ImplTrainingService trainingService;

    /**
     * Fetches all available training.
     *
     * @return a list of all trainings
     */

    @GetMapping("/trainings")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<Training> allTrainings() {
        return trainingService.getTrainings();
    }

    /**
     * Creates a new Training entity
     * The category of the training is validated by its ID before the training is created
     *
     * @param training the Training entity to be created.
     * @return ResponseEntity containing the saved Training object if successful
     * or bad request status, if the category is not found.
     */

    @PostMapping("/trainings")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Training> createTraining(@RequestBody Training training) {
        Optional<Category> category = trainingService.getCategoryById(training.getCategory().getId());
        if (category.isPresent()) {
            training.setCategory(category.get());
            Training savedTraining = trainingService.saveTraining(training);
            return ResponseEntity.ok(savedTraining);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a training by ist ID.
     *
     * @param id the ID of the training to be deleted.
     */

    @DeleteMapping("/trainings/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteTraining(@PathVariable("id") Long id) {
        trainingService.deleteTrainings(id);
    }

    /**
     * Fetches a Training entity by its ID.
     *
     * @param id the ID of the training.
     * @return a ResponseEntity containing the Training object if found,
     * or a not found status if the training doesn't exist.
     */

    @GetMapping("/trainings/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Training> getTrainingById(@PathVariable("id") Long id) {
        Optional<Training> training = trainingService.readTraining(id);
        if (training.isPresent()) {
            Training t = training.get();
            System.out.println("Formation récupérée : " + t.getName() + " - Catégorie : " + t.getCategory().getName());
            return new ResponseEntity<>(t, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Authenticates a user by email and password
     *
     * @param email    the email of the user
     * @param password the password of the user.
     * @return a ResponseEntity containing the User object if authentication is successfully
     * or a not found status if the credentials are incorrect
     */

//    @GetMapping("/users")
//    public ResponseEntity<User> getUserByEmailAndPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
//        System.out.println(email + " " + password);
//        Optional<User> user = trainingService.readUser(email);
//
//
//        if (user.isPresent() && user.get().getPassword().equals(password)) {
//
//            return new ResponseEntity<>(user.get(), HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }

    /**
     * Updates an existing Training entity
     * This method is a placeHolder and doesn't perform any actual update
     *
     * @param id       the ID of the training to be training
     * @param training the updated Training entity
     * @return a ResponseEntity containing the updated TrainingObject
     */

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Training> updateTraining(@PathVariable Long id, @RequestBody Training training) {
        return ResponseEntity.ok(training);
    }

    /**
     * Updates a Training entity by its ID
     *
     * @param id              the ID of the training to be updated
     * @param trainingDetails the details to update the training with
     * @return a ResponseEntity containing the updated Training object.
     */

    @PutMapping("/trainings/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Training> updateOneTraining(@PathVariable Long id, @RequestBody Training trainingDetails) {
        Training updateTraining = trainingService.updateTraining(id, trainingDetails);
        return ResponseEntity.ok(updateTraining);
    }

    /**
     * Fetches all available categories
     *
     * @return a list of all Categories
     */


    @GetMapping("/categories")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<Category> getAllCategories() {
        return trainingService.getAllCategories();
    }

    /**
     * Fetches a Category entity by its ID
     *
     * @param id the ID of the category
     * @return a ResponseEntity containing  the Category object if found
     * or a not found status if the category doesn't exist.
     */
    @GetMapping("/categories/{id}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        Optional<Category> category = trainingService.getCategoryById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
