import './home.scss';

import React from 'react';
import { Alert, Button, Card, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link } from 'react-router';

import { useAppSelector } from 'app/config/store';
import { ManagerworkDashboard } from 'app/features/managerwork';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { Authority } from 'app/shared/jhipster/constants';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const authorities = account.authorities ?? [];

  const isManager = hasAnyAuthority(authorities, [Authority.MANAGER]);
  const isAdmin = hasAnyAuthority(authorities, [Authority.ADMIN]);
  const isUser = hasAnyAuthority(authorities, [Authority.USER]);

  if (isManager) {
    return <ManagerworkDashboard />;
  }

  if (isAdmin) {
    return (
      <Row className="justify-content-center">
        <Col lg="10">
          <Card className="shadow-sm border-0">
            <Card.Body className="p-4">
              <h1 className="display-5 mb-3">Espace administration</h1>
              <p className="lead text-muted">
                Supervisez les utilisateurs, les droits d&apos;acces et les outils d&apos;administration de la plateforme.
              </p>
              <div className="d-flex flex-wrap gap-2 mt-4">
                <Button as={Link as any} to="/admin/user-management" variant="primary">
                  Gerer les utilisateurs
                </Button>
                <Button as={Link as any} to="/admin/metrics" variant="outline-secondary">
                  Voir les metriques
                </Button>
                <Button as={Link as any} to="/admin/health" variant="outline-secondary">
                  Etat du systeme
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    );
  }

  if (isUser) {
    return (
      <Row className="justify-content-center">
        <Col lg="10">
          <Card className="shadow-sm border-0">
            <Card.Body className="p-4">
              <h1 className="display-5 mb-3">Bienvenue sur CheckProfile</h1>
              <p className="lead text-muted">Accedez rapidement aux modules metier disponibles pour votre profil utilisateur.</p>
              <div className="d-flex flex-wrap gap-2 mt-4">
                <Button as={Link as any} to="/evaluation" variant="primary">
                  Ouvrir les evaluations
                </Button>
                <Button as={Link as any} to="/resultat" variant="outline-secondary">
                  Consulter les resultats
                </Button>
                <Button as={Link as any} to="/employe" variant="outline-secondary">
                  Voir les employes
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    );
  }

  return (
    <Row className="justify-content-center">
      <Col lg="8">
        <h1 className="display-4">
          <Translate contentKey="home.title">Bienvenue sur CheckProfile</Translate>
        </h1>
        <p className="lead">
          <Translate contentKey="home.subtitle">Connectez-vous pour acceder a votre espace selon votre role.</Translate>
        </p>

        {!isAuthenticated ? (
          <>
            <Alert variant="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">Si vous voulez </Translate>
              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> vous connecter</Translate>
              </Link>
              <Translate contentKey="global.messages.info.authenticated.suffix">, ouvrez votre session.</Translate>
            </Alert>
            <Alert variant="warning">
              <Translate contentKey="global.messages.info.register.noaccount">Vous n&apos;avez pas encore de compte ?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Creer un compte</Translate>
              </Link>
            </Alert>
          </>
        ) : (
          <Alert variant="info">
            Votre compte est connecte, mais aucun accueil specifique n&apos;est configure pour vos autorites actuelles.
          </Alert>
        )}
      </Col>
    </Row>
  );
};

export default Home;
