import { ApiResult, apiGet } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { IEpreuve } from 'app/shared/model/epreuve.model';

const BASE = getManagerworkApiEndpoint('epreuves');

export const fetchManagerworkEpreuvesByCompetence = (competenceId: number, signal?: AbortSignal): Promise<ApiResult<IEpreuve[]>> =>
  apiGet<IEpreuve[]>(`${BASE}/competence/${competenceId}`, { signal });

/** xport const fetchManagerworkCompetencesByPoste = (posteId: number, signal?: AbortSignal): Promise<ApiResult<ICompetenceRequise[]>> =>
    apiGet<ICompetenceRequise[]>(`${BASE}/poste/${posteId}`, { signal }); */
