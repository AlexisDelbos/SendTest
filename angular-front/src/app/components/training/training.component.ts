import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from 'src/app/model/category.model';
import { Training } from 'src/app/model/training.model';
import { ApiService } from 'src/app/services/api.service';
import { AuthenticateService } from 'src/app/services/authenticate.service';

@Component({
  selector: 'app-training',
  templateUrl: './training.component.html',
  styleUrls: ['./training.component.css']
})

/**
 * Composant de gestion d'une formation à ajouter en base ou à mettre à jour
 */
export class TrainingComponent implements OnInit {
  myForm: FormGroup;
  training: Training;
  error: string = "";
  status: boolean = false;

  constructor(private formBuilder: FormBuilder, private apiService: ApiService,
    private router: Router, private route: ActivatedRoute, public authService: AuthenticateService) {
    this.training = new Training(0, "", "", 0, 0, new Category(0, ""));
    this.myForm = this.formBuilder.group({
      id: [this.training.id],
      name: ["", Validators.required],
      description: [this.training.description, Validators.required],
      price: [this.training.price, [Validators.required, Validators.min(0)]],
      category: ["", Validators.required]
    });

  }

  /**
   * La méthode d'initialisation permet ici de récupérer l'id en cas de mise à jour, afin de récupérer les données associées via l'api
   * dans le cas contraire, il s'agit d'une création de formation 
   */
  categories: Category[] = [];

  ngOnInit(): void {
    let id = this.route.snapshot.params['id'];

    this.apiService.getCategories().subscribe({
      next: (data) => {
        this.categories = data;
      },
      error: (err) => this.error = err.message
    });

    if (id > 0) {
      this.status = true;
      this.apiService.getTraining(id).subscribe({
        next: (data) => {
          this.training = data;
          this.myForm.setValue({
            id: this.training.id,
            name: this.training.name,
            description: this.training.description,
            price: this.training.price,
            category: this.training.category.id
          });
        },
        error: (err) => this.error = err.message
      });
    }
  }


  /**
   * Méthode d'ajout (ou de mise à jour) d'une nouvelle formation (en fonction du contexte d'appel : présence de l'id ?)
   * @param form comprend le formulaire avec toutes les données saisies par l'utilisateur
   */
  onAddTraining(form: FormGroup) {
    if (form.valid) {
      const categoryId = form.value.category;
      if (!categoryId) {
        this.error = 'la catégorie est obligatoire';
        return;
      }
      const category = { id: categoryId };
      if (this.status) {
        this.updateTraining(form);
      } else {

        this.apiService.postTraining({
          name: form.value.name,
          description: form.value.description,
          price: form.value.price,
          quantity: 1,
          category: category
        }).subscribe({
          next: (data) => console.log(data),
          error: (err) => {
            console.error('Error:', err);
            this.error = err.message;
          },
          complete: () => this.router.navigateByUrl('trainings')
        });
      }
    } else {
      this.error = 'pb de saisi';
    }
  }



  /**
   * Méthode de mise à jour d'une nouvelle formation
   * @param form comprend le formulaire avec toutes les données saisies par l'utilisateur
   */
  updateTraining(form: FormGroup) {
    if (form.valid) {
      const category = { id: form.value.category };
      this.apiService.putTraining({
        id: form.value.id,
        name: form.value.name,
        description: form.value.description,
        price: form.value.price,
        quantity: 1,
        category: category
      }).subscribe({
        next: (data) => console.log(data),
        error: (err) => this.error = err.message,
        complete: () => this.router.navigateByUrl('trainings')
      });
    } else {
      this.error = 'pb de saisi';
    }
  }


}
