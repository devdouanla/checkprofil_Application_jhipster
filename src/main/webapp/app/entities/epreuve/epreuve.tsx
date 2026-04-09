import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';

import { getEntities } from './epreuve.reducer';

export const Epreuve = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const epreuveList = useAppSelector(state => state.epreuve.entities);
  const loading = useAppSelector(state => state.epreuve.loading);
  const totalItems = useAppSelector(state => state.epreuve.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="epreuve-heading" data-cy="EpreuveHeading">
        <Translate contentKey="checkprofileApp.epreuve.home.title">Epreuves</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="checkprofileApp.epreuve.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/epreuve/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="checkprofileApp.epreuve.home.createLabel">Create new Epreuve</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {epreuveList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="checkprofileApp.epreuve.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('titre')}>
                  <Translate contentKey="checkprofileApp.epreuve.titre">Titre</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('titre')} />
                </th>
                <th className="hand" onClick={sort('enonce')}>
                  <Translate contentKey="checkprofileApp.epreuve.enonce">Enonce</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('enonce')} />
                </th>
                <th className="hand" onClick={sort('difficulte')}>
                  <Translate contentKey="checkprofileApp.epreuve.difficulte">Difficulte</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('difficulte')} />
                </th>
                <th className="hand" onClick={sort('duree')}>
                  <Translate contentKey="checkprofileApp.epreuve.duree">Duree</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('duree')} />
                </th>
                <th className="hand" onClick={sort('nbQuestions')}>
                  <Translate contentKey="checkprofileApp.epreuve.nbQuestions">Nb Questions</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nbQuestions')} />
                </th>
                <th className="hand" onClick={sort('genereParIA')}>
                  <Translate contentKey="checkprofileApp.epreuve.genereParIA">Genere Par IA</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('genereParIA')} />
                </th>
                <th className="hand" onClick={sort('publie')}>
                  <Translate contentKey="checkprofileApp.epreuve.publie">Publie</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('publie')} />
                </th>
                <th>
                  <Translate contentKey="checkprofileApp.epreuve.competence">Competence</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {epreuveList.map(epreuve => (
                <tr key={`entity-${epreuve.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/epreuve/${epreuve.id}`} variant="link" size="sm">
                      {epreuve.id}
                    </Button>
                  </td>
                  <td>{epreuve.titre}</td>
                  <td>{epreuve.enonce}</td>
                  <td>
                    <Translate contentKey={`checkprofileApp.Difficulte.${epreuve.difficulte}`} />
                  </td>
                  <td>{epreuve.duree}</td>
                  <td>{epreuve.nbQuestions}</td>
                  <td>{epreuve.genereParIA ? 'true' : 'false'}</td>
                  <td>{epreuve.publie ? 'true' : 'false'}</td>
                  <td>{epreuve.competence ? <Link to={`/competence/${epreuve.competence.id}`}>{epreuve.competence.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/epreuve/${epreuve.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        as={Link as any}
                        to={`/epreuve/${epreuve.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        variant="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/epreuve/${epreuve.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        variant="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="checkprofileApp.epreuve.home.notFound">No Epreuves found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={epreuveList && epreuveList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Epreuve;
