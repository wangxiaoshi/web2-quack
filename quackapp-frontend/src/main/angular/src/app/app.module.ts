import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UiModule } from './ui/ui.module';
import { HttpClientModule } from '@angular/common/http';
import { QuacksComponent, QuacksModalContent } from './quacks/quacks.component';
import { CommentsComponent } from './comments/comments.component';
import { ProfileViewComponent } from './profile-view/profile-view.component';
import { PostComponent } from './post/post.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import {QuackRService} from './quackr.service';
import { AdminPanelComponent } from './admin-panel/admin-panel.component';
import {FormsModule} from '@angular/forms';
import {AuthenticationService} from './authentication.service';
import { EditQuackComponent } from './edit-quack/edit-quack.component';
import { DeleteQuackComponent } from './delete-quack/delete-quack.component';
import { ProfileEditComponent } from './profile-edit/profile-edit.component';


@NgModule({
  declarations: [
    AppComponent,
    QuacksComponent,
    QuacksModalContent,
    CommentsComponent,
    ProfileViewComponent,
    PostComponent,
    LoginComponent,
    SignupComponent,
    AdminPanelComponent,
    EditQuackComponent,
    DeleteQuackComponent,
    ProfileEditComponent,
  ],
  entryComponents: [QuacksModalContent],
  imports: [
      NgbModule.forRoot(),
      BrowserModule,
      AppRoutingModule,
      UiModule,
      HttpClientModule,
      FormsModule
  ],
  providers: [QuackRService, AuthenticationService],
  bootstrap: [AppComponent]
})
export class AppModule { }
