import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { QuacksComponent } from './quacks/quacks.component';
import { ProfileViewComponent } from './profile-view/profile-view.component';
import { ProfileEditComponent } from "./profile-edit/profile-edit.component";
import { PostComponent } from './post/post.component';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import { EditQuackComponent } from './edit-quack/edit-quack.component';
import {DeleteQuackComponent} from "./delete-quack/delete-quack.component";

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: QuacksComponent },
  { path: 'profile', component: ProfileViewComponent },
  { path: 'profile/edit', component: ProfileEditComponent },
  { path: 'post', component: PostComponent},
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'admin-panel', component: AdminPanelComponent },
  { path: 'edit-quack/:id', component: EditQuackComponent },
  { path: 'delete-quack/:id', component: DeleteQuackComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
