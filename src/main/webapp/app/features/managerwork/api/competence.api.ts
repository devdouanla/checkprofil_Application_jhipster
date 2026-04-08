import { ApiResult, apiGet } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { ICompetenceRequise } from 'app/shared/model/competence-requise.model';

const BASE = getManagerworkApiEndpoint('competenceRequises');

/** Backend: GET /api/competence-requises/poste/{id} → findByPosteId (pas de filtre query sur GET liste). */
export const fetchManagerworkCompetencesByPoste = (posteId: number, signal?: AbortSignal): Promise<ApiResult<ICompetenceRequise[]>> =>
  apiGet<ICompetenceRequise[]>(`${BASE}/poste/${posteId}`, { signal });
