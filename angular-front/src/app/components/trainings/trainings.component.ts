import { Component, OnInit } from '@angular/core';
import { Training } from 'src/app/model/training.model';
import { CartService } from 'src/app/services/cart.service';
import { Router } from '@angular/router';
import { ApiService } from 'src/app/services/api.service';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { Category } from 'src/app/model/category.model';

@Component({
  selector: 'app-trainings',
  templateUrl: './trainings.component.html',
  styleUrls: ['./trainings.component.css']
})

/**
 * Composant de gestion des formations permettant l'affichage et l'ajout dans le panier de formation
 * En fonction de ses roles, l'utilisateur aura + ou - de droits (User ou Admin) dans l'application
 * delors, il aura accès à des fonctionnalités spécifiques suplémentaires 
 */
export class TrainingsComponent implements OnInit {
  listTrainings : Training[] | undefined;
  listCategories: Category[] | undefined;
  filteredTrainings: Training[] | undefined; // Formations affichées après filtrage
  error = null;
  
  constructor(private cartService : CartService, private router : Router, 
    private apiService : ApiService, public authService : AuthenticateService) {
  }

  ngOnInit(): void {
    this.getAllTrainings();
    this.getAllCategories();
  }

  /**
   * Méthode qui renvoi à partir de l'Api toutes les formations accessibles
   * en cas de problème avec l'api, un message d'erreur sera relayé et affiché
   */
  getAllTrainings() {
    this.apiService.getTrainings().subscribe({
      next: (data) => {
        this.listTrainings = data;
        this.filteredTrainings = data; // Initialisation de filteredTrainings
      },
      error: (err) => (this.error = err.message),
      complete: () => (this.error = null),
    });
  }
  getAllCategories() {
    this.apiService.getCategories().subscribe({
      next: (data) => this.listCategories = data,
      error: (err) => this.error = err.message,
      complete: () => this.error = null
    });
  }

    /**
   * Méthode pour filtrer les formations en fonction de la catégorie sélectionnée.
   * @param categoryId Identifiant de la catégorie à filtrer
   */
    filterByCategory(categoryId: number): void {
      if (this.listTrainings) {
        this.filteredTrainings = this.listTrainings.filter(
          (training) => training.category.id === categoryId
        );
      }
    }

  /**
   * Méthode pour réinitialiser le filtre et afficher toutes les formations.
   */
  resetFilter(): void {
    this.filteredTrainings = this.listTrainings;
  }

  /**
   * Méthode permettant à tous l'ajout d'une formation au panier en utilisant le service dédié
   * @param training 
   */
  onAddToCart(training:Training){
    if(training.quantity > 0) {
     this.cartService.addTraining(training);
     //this.router.navigateByUrl('cart');
    }
  }

  /**
   * Méthode permettant uniquement à l'Admin de supprimer une formation de l'ensemble des formations
   * Une fois la formation supprimée dans l'api, la liste des formations est raffraichie
   * @param training 
   */
  onDeleteTraining(training : Training){
    if(confirm("vous êtes sur de vouloir supprimer cette formation")) {
      this.apiService.delTraining(training).subscribe({
        next : (data) => console.log(data),
        error : (err) => this.error = err.message,
        complete : () => this.getAllTrainings()
      })
    }
  }

  /**
   * Méthode permettant uniquement à l'Admin de mettre à jour une formation de l'ensemble des formations
   * En renvoyant ici vers le composant dédié à la mise à jour
   * @param training 
   */
  onUpdateTraining(training : Training){
    this.router.navigateByUrl('training/' + training.id);
  }

  /**
   * Méthode qui empeche des saisies incorrectes sur la quantité de formation
   * @param training la formation concerné
   */
  validateQuantity(training : Training) {
    if (training.quantity < 1) {
      training.quantity = 1;
    } else if (training.quantity > 10) {
      training.quantity = 10;
    }
  }
}



