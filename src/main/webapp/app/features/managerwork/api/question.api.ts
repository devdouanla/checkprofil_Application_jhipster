import { ApiResult, apiGet } from './api-store';
import { getManagerworkApiEndpoint } from '../managerwork.config';

import { IQuestion } from 'app/shared/model/question.model';

const BASE = getManagerworkApiEndpoint('questions');
export const fetchManagerworkquestionsByCompetenceAndDifficulty = (
  competenceId?: number,
  difficulte?: string,
  page = 0,
  size = 20,
  signal?: AbortSignal,
): Promise<ApiResult<IQuestion[]>> =>
  apiGet<IQuestion[], any>(BASE, {
    params: {
      page,
      size,
      ...(competenceId && { 'competenceId.equals': competenceId }),
      ...(difficulte && { 'difficulte.equals': difficulte }),
    },
    signal,
  });

/**
 * Fisher-Yates shuffle for reproducible randomization (seed optional from session ID)
 */
function shuffleArray<T>(array: T[], seed?: number): T[] {
  const shuffled = [...array];
  let m = array.length;
  let s = seed ?? Math.floor(Math.random() * 65536);
  while (m) {
    s = ((s * 1103515245 + 12345) % 2147483647) / 2147483647;
    const i = Math.floor(s * m--);
    [shuffled[m], shuffled[i]] = [shuffled[i], shuffled[m]];
  }
  return shuffled;
}
// export const fetchManagerworkquestionsByCompetenceAndDifficulty = (): Promise<ApiResult<IQuestion[]>> => apiGet<IQuestion[], any>(BASE);

/**
 * Fetch all questions for epreuve, shuffle randomly, return first nbQuestions (or all if not specified)
 * @param epreuveId - Epreuve ID
 * @param nbQuestions - Number to draw (from epreuve.nbQuestions)
 * @param competenceId - Optional competence ID for filtering (if needed in future)
 * @param  difficulty - Optional difficulty level for filtering (if needed in future)
 * @param seed - Optional reproducible seed (e.g., session.id)
 *
 */
export const fetchRandomquestionsByCompetenceAndDifficulty = async (
  competenceId?: number,
  difficulte?: string,
  nbQuestions: number = 5,
  seed?: number,
): Promise<IQuestion[]> => {
  const result = await fetchManagerworkquestionsByCompetenceAndDifficulty(competenceId, difficulte);
  const allQuestions = result.data ?? [];
  const shuffled = shuffleArray(allQuestions, seed);
  return shuffled.slice(0, nbQuestions);
};
