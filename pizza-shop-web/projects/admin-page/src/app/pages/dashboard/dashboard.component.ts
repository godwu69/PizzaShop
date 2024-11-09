import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../../../shared/src/lib/services/user.service';
import {Router} from '@angular/router';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgxPaginationModule} from 'ngx-pagination';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    NgForOf,
    FormsModule,
    NgxPaginationModule,
    DatePipe,
    NgIf
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  constructor(private userService: UserService, private router: Router) {
  }

  listUsers: any[] = [];
  selectedUser: any = {};
  page: number = 1;
  isEditPopupOpen = false;
  isLoading: boolean = false;

  getAllUsers(){
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (res) => {
        this.listUsers = res.data;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {
        this.isLoading = false;
      }
    })
  }

  editUser(){
    const index = this.listUsers.findIndex(u => u.user_id === this.selectedUser.user_id);
    if (index > -1) {
      this.listUsers[index] = { ...this.selectedUser };
    }
    this.userService.updateUser(this.selectedUser).subscribe({
      next: (res) => {
        console.log(res.message);
        this.closeEditPopup();
        this.getAllUsers();
      },
      error: (err) => {
        console.log(err.message);
        this.closeEditPopup();
      }
    });
  }

  openEditPopup(user: any) {
    this.selectedUser = { ...user };
    this.isEditPopupOpen = true;
  }

  closeEditPopup() {
    this.isEditPopupOpen = false;
  }


  deleteUser(user: any){
    this.userService.deleteUser(user.user_id).subscribe({
      next: (res) => {
        console.log(res.message);
        this.getAllUsers();
      },
      error: (err) => {
        console.log(err.message);
      }
    });
  }


  ngOnInit(): void {
    this.getAllUsers();
  }

  updateUserStatus(user: any) {
    const userId = user.user_id;
    const updatedStatus = user.status;
    const newUser = {user_id: userId, status: updatedStatus}
    this.userService.updateUser(newUser).subscribe({
      next: (res) => {
        console.log('Status updated:', res.message);
        this.getAllUsers();
      },
      error: (err) => {
        console.log('Error updating status:', err.message);
      }
    });
  }

  updateUserRole(user: any) {
    const userId = user.user_id;
    const updatedRole = user.role;
    const newUser = {user_id: userId, role: updatedRole}
    this.userService.updateUser(newUser).subscribe({
      next: (res) => {
        console.log('Status updated:', res.message);
        this.getAllUsers();
      },
      error: (err) => {
        console.log('Error updating status:', err.message);
      }
    });
  }

}
