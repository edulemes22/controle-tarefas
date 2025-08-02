import { Routes } from '@angular/router';
import { ListaTarefa } from './components/lista-tarefa/lista-tarefa';
import { FormularioTarefa } from './components/formulario-tarefa/formulario-tarefa';

export const routes: Routes = [
  { path: '', component: ListaTarefa },
  { path: 'new', component: FormularioTarefa },
  { path: 'edit/:id', component: FormularioTarefa },
  { path: '**', redirectTo: '' }
];
