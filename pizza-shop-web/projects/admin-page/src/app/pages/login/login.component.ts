import {Component, OnInit} from '@angular/core';
import {NgClass, NgIf, NgOptimizedImage} from '@angular/common';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../../../../../shared/src/lib/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    NgOptimizedImage,
    NgIf,
    RouterLink,
    RouterLinkActive,
    ReactiveFormsModule,
    NgClass
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {
  }

  form!: FormGroup;
  isShowPassword: boolean = false;
  submitted: boolean = false;
  isLoading: boolean = false;
  message: string = '';

  toggleShowPassword() {
    this.isShowPassword = !this.isShowPassword;
  }

  login() {
    this.submitted = true;
    this.isLoading = true;
    if (this.form.invalid) {
      this.isLoading = false;
      return;
    }
    const data = this.form.getRawValue();
    return this.authService.login(data).subscribe({
      next: (res) => {
        console.log(res);
        localStorage.setItem('token', res.data.access_token);
        localStorage.setItem('refreshToken', res.data.refresh_token);

        this.showMessage('Login successfully!', '/dashboard');
      },
      error: (err) => {
        console.log(err);
        this.showMessage(err.message, null);
      },
      complete: () => {
        this.isLoading = false;
      }
    })
  }

  InitForm() {
    this.form = this.formBuilder.group({
      username: [
        null,
        [Validators.required, Validators.minLength(6), Validators.pattern('^[A-Za-z0-9]+$')],
      ],
      password: [
        null,
        [Validators.required, Validators.minLength(6), Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$')],
      ]
    });
  }

  ngOnInit(): void {
    this.InitForm();
  }

  showMessage(message: string, path: any) {
    this.message = message;
    setTimeout(() => {
      this.message = '';
      if (path != null){
        this.router.navigate([path]);
      }
    }, 2000);
  }
}
