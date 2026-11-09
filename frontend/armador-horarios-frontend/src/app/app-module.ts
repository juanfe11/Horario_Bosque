import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { NavbarComponent } from './component/navbar/navbar';
import { ArmadorPrincipalComponent } from './component/armador-principal/armador-principal';

@NgModule({
  declarations: [
    App,
    NavbarComponent,
    ArmadorPrincipalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    CommonModule
  ],
  providers: [provideHttpClient()],
  bootstrap: [App]
})
export class AppModule { }
