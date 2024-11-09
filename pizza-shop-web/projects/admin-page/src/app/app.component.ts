import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {NavbarComponent} from './templates/navbar/navbar.component';
import {SidebarComponent} from './templates/sidebar/sidebar.component';
import {UserService} from '../../../shared/src/lib/services/user.service';
import {NgIf} from '@angular/common';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent, SidebarComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'admin-page';
  constructor(private userService: UserService, private router: Router) {
  }

  isLoggedIn: boolean = true;

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const url = event.urlAfterRedirects;
        this.isLoggedIn = !(url.includes('/login') || url.includes('/register'));
      }
    });
  }

}
