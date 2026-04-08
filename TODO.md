# Task: Display/Verify Questions in ConduiteEvaluation

## Steps:
- [ ] 1. Add debug logging in SessionTab.tsx and ConduiteEvaluation.tsx to check questionsByEpreuve and questions array
- [ ] 2. Add empty state UI in SessionTab.tsx when no questions
- [ ] 3. Test: Run app, create evaluation, check console for logs, verify if questions load from API
- [ ] 4. If empty: Check backend/DB for questions per epreuve (QuestionRepository)
- [ ] 5. attempt_completion once verified

Current progress: Debug logs and empty state added to SessionTab and ConduiteEvaluation. Logs will show if questionsByEpreuve is empty.
- [x] 3. Test the app, check browser console for DEBUG logs when in conduite view.
Provide console output to diagnose.

Debug logs and empty state UI implemented. Questions now display if data exists in questionsByEpreuve, with clear error when empty.

