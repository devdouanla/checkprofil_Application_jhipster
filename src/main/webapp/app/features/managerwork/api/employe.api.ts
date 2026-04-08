import { ApiResult, apiGet } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { IEmploye } from 'app/shared/model/employe.model';
import { IPoste } from 'app/shared/model/poste.model';

const EMPLOYE_BASE = getManagerworkApiEndpoint('employes');
const POSTE_BASE = getManagerworkApiEndpoint('postes');

export const fetchManagerworkEmployesByManager = (managerId: number, signal?: AbortSignal): Promise<ApiResult<IEmploye[]>> =>
  apiGet<IEmploye[], { 'posteManagerId.equals': number; 'actif.equals': boolean; size: number; sort: string }>(EMPLOYE_BASE, {
    params: { 'posteManagerId.equals': managerId, 'actif.equals': true, size: 200, sort: 'id,desc' },
    signal,
  });

export const fetchManagerworkPostesByManager = (managerId: number, signal?: AbortSignal): Promise<ApiResult<IPoste[]>> =>
  apiGet<IPoste[], { 'managerId.equals': number; size: number; sort: string }>(POSTE_BASE, {
    params: { 'managerId.equals': managerId, size: 200, sort: 'nom,asc' },
    signal,
  });
