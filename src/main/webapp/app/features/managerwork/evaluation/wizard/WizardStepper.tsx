import React from 'react';

import { ManagerworkWizardStep } from '../../types/managerwork.types';

interface ManagerworkWizardStepperProps {
  activeStep: ManagerworkWizardStep;
}

const steps: { step: ManagerworkWizardStep; title: string; subtitle: string }[] = [
  { step: 1, title: 'Employe', subtitle: 'Choisir le collaborateur' },
  { step: 2, title: 'Competences', subtitle: 'Cadrer le besoin' },
  { step: 3, title: 'Epreuves', subtitle: 'Composer la session' },
  { step: 4, title: 'Recap', subtitle: 'Valider et lancer' },
];

export const ManagerworkWizardStepper = ({ activeStep }: ManagerworkWizardStepperProps) => (
  <div className="mw-stepper mb-4">
    {steps.map(item => {
      const statusClass = activeStep === item.step ? 'is-active' : activeStep > item.step ? 'is-completed' : '';
      return (
        <div key={item.step} className={`mw-step ${statusClass}`}>
          <div className="d-flex align-items-center gap-3">
            <span className="mw-step-indicator">{item.step}</span>
            <div>
              <div className="fw-semibold">{item.title}</div>
              <div className="small text-muted">{item.subtitle}</div>
            </div>
          </div>
        </div>
      );
    })}
  </div>
);
