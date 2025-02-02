import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user.model';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-loginout',
  templateUrl: './loginout.component.html',
  styleUrls: ['./loginout.component.css']
})

/**
 * Composant de gestion de l'authentification d'un utilisateur
 */
export class LoginoutComponent implements OnInit {
  myForm: FormGroup;
  user: User | undefined;
  error: string | undefined;
  connected: boolean = false;

  constructor(private formBuilder: FormBuilder, public authService: AuthenticateService, private router: Router) {
    this.user = authService.getUser();
    this.connected = authService.isConnected();
    this.myForm = this.formBuilder.group({
      email: [this.user.email, [Validators.required, Validators.pattern(environment.regExEmail)]],
      password: [this.user.password, [Validators.required]]
    })
  }

  ngOnInit(): void {
    this.user = new User(0, "", "", { id: 0, role: "" });
  }

  /**
   * Méthode de demande d'authentification à partir des credentials (login + pwd) saisis et comparer avec l'api 
   * En cas de succès, la session de l'utilisateur reste jusqu'à la déconnexion
   * @param form
   */
  onLogin(form: FormGroup) {
    if (form.valid) {
      try {
        this.authService.login(form.value.email, form.value.password).subscribe({
          next: (data) => {
            if (data) {
              console.log("Test de la data", data);
              this.user = data;
              this.authService.setUser(this.user);
              this.router.navigate(['trainings']);
            } else {
              this.error = "Email ou mot de passe incorrect";
            }
          },
          error: () => this.error = "Erreur API !",
          complete: () => console.log("Welcome")
        });
      } catch (exception) {
        this.error = "Erreur imprévue, Réessayer";
      }
    } else {
      this.error = 'Erreur de saisie du formulaire';
    }
  }
  /**
   * ToDo Ajouter un nouvel utilisateur
   */
  onAddUser() {

  }

  goToHome(): void {
    this.router.navigateByUrl('/');
  }
  

  /**
   * Méthode de déconnexion d'un utilisateur
   */
  disconnect() {
    this.authService.disconnected();
    this.connected = false;
    this.router.navigateByUrl('trainings');
  }
}
