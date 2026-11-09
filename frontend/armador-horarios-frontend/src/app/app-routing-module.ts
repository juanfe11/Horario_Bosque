import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArmadorPrincipalComponent } from './component/armador-principal/armador-principal';

const routes: Routes = [
  { path: '', redirectTo: 'horario', pathMatch: 'full' },
  { path: 'horario', component: ArmadorPrincipalComponent },
  { path: '**', redirectTo: 'horario' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
