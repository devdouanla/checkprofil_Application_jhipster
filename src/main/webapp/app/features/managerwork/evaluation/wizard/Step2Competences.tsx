import React from 'react';
import { ICompetenceRequise } from 'app/shared/model/competence-requise.model';

interface ManagerworkStep2CompetencesProps {
  competences: ICompetenceRequise[];
  selectedCompetenceIds: number[];
  onToggle: (competenceId: number) => void;
}

export const ManagerworkStep2Competences = ({ competences, selectedCompetenceIds, onToggle }: ManagerworkStep2CompetencesProps) => (
  <div
    style={{
      display: 'grid',
      gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))',
      gap: '1rem',
      padding: '0.5rem 0',
    }}
  >
    {competences.map(item => {
      const competenceId = item.competence?.id;
      const selected = competenceId ? selectedCompetenceIds.includes(competenceId) : false;
      const obligatoire = item.obligatoire;

      return (
        <div
          key={item.id}
          onClick={() => competenceId && onToggle(competenceId)}
          style={{
            background: selected ? '#eef4ff' : '#fff',
            border: selected ? '2px solid #4361ee' : '1.5px solid #e2e8f0',
            borderRadius: '12px',
            padding: '1.1rem 1.25rem',
            cursor: competenceId ? 'pointer' : 'default',
            transition: 'border-color 0.18s, box-shadow 0.18s, background 0.18s',
            boxShadow: selected ? '0 0 0 4px rgba(67,97,238,0.10)' : '0 1px 3px rgba(0,0,0,0.06)',
            position: 'relative',
            userSelect: 'none',
          }}
        >
          {/* Indicateur sélection coin haut-droit */}
          {selected && (
            <span
              style={{
                position: 'absolute',
                top: '10px',
                right: '12px',
                background: '#4361ee',
                color: '#fff',
                borderRadius: '20px',
                fontSize: '11px',
                fontWeight: 600,
                padding: '2px 10px',
                letterSpacing: '0.02em',
              }}
            >
              ✓ Retenu
            </span>
          )}

          {/* Nom compétence */}
          <div
            style={{
              fontWeight: 600,
              fontSize: '15px',
              color: selected ? '#2d3a8c' : '#1a202c',
              marginBottom: '0.5rem',
              paddingRight: selected ? '72px' : '0',
            }}
          >
            {item.competence?.nom ?? 'Compétence non renseignée'}
          </div>

          {/* Séparateur */}
          <div style={{ height: '1px', background: selected ? '#c7d5f8' : '#f0f0f0', margin: '0.5rem 0' }} />

          {/* Type */}
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginTop: '0.4rem' }}>
            <span
              style={{
                display: 'inline-block',
                fontSize: '12px',
                fontWeight: 500,
                padding: '3px 10px',
                borderRadius: '20px',
                background: obligatoire ? '#fff3e0' : '#f0fdf4',
                color: obligatoire ? '#b45309' : '#166534',
                border: `1px solid ${obligatoire ? '#fbbf24' : '#86efac'}`,
              }}
            >
              {obligatoire ? '★ Obligatoire' : 'Optionnelle'}
            </span>

            {!selected && <span style={{ fontSize: '12px', color: '#94a3b8' }}>Cliquer pour sélectionner</span>}
          </div>
        </div>
      );
    })}
  </div>
);
