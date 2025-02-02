import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/model/user.model';
import { ApiService } from 'src/app/services/api.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent {
  myForm: FormGroup;
  error: string = '';
  successMessage: string = '';
  user: User | undefined;

  constructor(private fb: FormBuilder, private apiService: ApiService) {
    this.myForm = this.fb.group({
      email: ['', [Validators.required, Validators.email, Validators.pattern(environment.regExEmail)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    },
      { validators: this.passwordMatchValidator }
    );
  }

  /**
   * Après avoir indiqué les contraintes pour chacun de ses champs, l'objet myForm est utilisé coté html 
   * pour chaque input, des vérifications sont effectuées afin d'afficher des messages d'erreur en cas de saisie incorrecte.
   * Lorsque le formulaire est soumis et respecte les contraintes, 
   * une méthode côté Model(Ts,Logique) prendre le relais pour vérifier si les 2 mot de passe sont identiques 
   * @param form le formulaire Angular
   * @returns `null` si les mots de passe correspondent, ou un objet avec l'erreur `passwordMismatch` sinon.
   * En fonction du retour de cette méthode, soit aucun message n'est affiché (`null`), 
   * soit un message d'erreur est déclenché si les mots de passe ne correspondent pas (`passwordMismatch`).
   */
  passwordMatchValidator(form: AbstractControl) { //AbstractControl est la classe mère de FormControl, FormGroup et FormArray
    //du coup, AC permet de rendre la validation + générique et extensible : Polymorphisme !
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onAddUser(form: FormGroup): void {
    if (form.valid) {
      const userRole = { id: 1, role: 'USER' };
      this.user = new User(0, form.value.email, form.value.password, userRole);

      this.apiService.postUser(this.user).subscribe({
        next: (data) => {
          this.successMessage = 'Utilisateur créé avec succès !';
          console.log(data);
        },
        error: () => this.error = "Pb Api !",
        complete: () => setTimeout(() => {
          this.successMessage = '';
          this.error = '';
        }, 5000)
      });
    } else {
      this.error = 'Veuillez corriger les erreurs du formulaire.';
      this.successMessage = '';
    }
  }


}
