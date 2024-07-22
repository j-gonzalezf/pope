import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Table from 'react-bootstrap/Table';
import { BsCheckSquareFill, BsFillPlusCircleFill, BsPencilSquare, BsTrash, BsXSquareFill } from "react-icons/bs";
import './TemplateView.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import AddTemplateRow from './AddTemplateRow';
import EditTemplateRow from './EditTemplateRow';

const TemplateView = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { clientId } = useParams();
    const { cycleId } = useParams();
    const { templateId } = useParams();

    const getTemplate = useSelector(selectors.getTemplate);
    const getTemplateRows = useSelector(selectors.getTemplateRows);

    const [name, setName] = useState(getTemplate.name);
    const [error, setError] = useState(null);
    const [showAddInput, setShowAddInput] = useState(false);
    const [showUpdateInput, setShowUpdateInput] = useState(false);
    const [editingRowId, setEditingRowId] = useState(null);

    let form1;

    const handleUpdateTemplate = event => {

        event.preventDefault();

        if (form1.checkValidity()) {
            dispatch(actions.updateTemplate(
                {
                    id: templateId,
                    name: name,
                    creationDate: getTemplate.creationDate,
                    cycleId: cycleId
                },
                () => {
                    setShowUpdateInput(false);
                    dispatch(actions.getTemplate(templateId,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)));
        }

    }

    const handleDeleteTemplate = () => {
        dispatch(actions.deleteTemplate(templateId,
            () => {
                navigate(`/templates/${clientId}/trainingCycle/${cycleId}`);
            },
            errors => setError(errors)
        ));
    }

    const handleDeleteRow = row => {
        dispatch(actions.deleteTemplateRow(templateId, row.id,
            () => {
                dispatch(actions.getTemplateRows(templateId,
                    () => { },
                    errors => setError(errors)));
            },
            errors => setError(errors)
        ));
    }

    useEffect(() => {
        dispatch(actions.getTemplate(templateId,
            () => {
                dispatch(actions.getTemplateRows(templateId,
                    () => { },
                    errors => setError(errors)));
            },
            errors => setError(errors)));
    }, [dispatch, templateId]);

    return (

        <div fluid="true" className='TemplateView'>

            <Row>

                <Col xs={12} sm={12} md={9} className='template'>
                    <h3 className="title templates">
                        <FormattedMessage id="project.templates.templateView.title" />
                    </h3>

                    <div className="table-responsive">
                        <Table className="table">
                            <thead>
                                <tr>
                                    <th className="customTable title-template">
                                        {showUpdateInput ? (
                                            <Form
                                                ref={node => form1 = node}
                                                className="needs-validation"
                                                noValidate
                                                onSubmit={e => handleUpdateTemplate(e)}
                                            >
                                                <Form.Group className="mb-3 template m-0">
                                                    <Form.Control
                                                        type="text"
                                                        className="form-control"
                                                        id="name"
                                                        name="name"
                                                        placeholder="Nombre de la plantilla"
                                                        value={name}
                                                        onChange={e => setName(e.target.value)}
                                                        required
                                                        autoFocus
                                                    />
                                                    <div className="form-buttons">
                                                        <Button className="primary template" type="submit">
                                                            <BsCheckSquareFill className="checkIconStyle" color='#e6af2e' size={20} />
                                                        </Button>
                                                        <Button className="primary template" onClick={() => { setShowUpdateInput(false); setName('') }}>
                                                            <BsXSquareFill className="crossIconStyle" color='gray' size={20} />
                                                        </Button>
                                                    </div>
                                                </Form.Group>

                                                <Errors errors={error} onClose={() => setError(null)} />

                                            </Form>
                                        ) : (
                                            <div className="form-buttons name">
                                                <h4 className="d-flex align-items-center m-0">
                                                    <Link to={`/templates/${clientId}/trainingCycle/${cycleId}`} className='link h'>{getTemplate.name}</Link>
                                                </h4>
                                                <Button className="primary template name" title='Editar nombre de plantilla' onClick={() => { setShowUpdateInput(true); setName(getTemplate.name) }}>
                                                    <BsPencilSquare className="checkIconStyle" color='#e6af2e' size={20} />
                                                </Button>
                                                <Button className="primary template delete name" title='Eliminar plantilla' onClick={handleDeleteTemplate} >
                                                    <BsTrash className="crossIconStyle" color='red' size={20} />
                                                </Button>
                                            </div>
                                        )}
                                    </th>
                                </tr>
                                <tr>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.exercise" /></th>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.series" /></th>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.reps" /></th>
                                    <th className="customTable underline"><FormattedMessage id="project.templates.templateView.weight" /></th>
                                </tr>
                            </thead>
                            <tbody>
                                {getTemplateRows.map((row, index) => (
                                    <tr key={index}>
                                        {editingRowId === row.id ? (
                                            <td colSpan={5} className="customTable">
                                                <EditTemplateRow row={row} onClose={() => setEditingRowId(null)} />
                                            </td>
                                        ) : (
                                            <>
                                                <td className="customTable">
                                                    <Link to={'/templates/exercises'} className="link h" >
                                                        {row.exerciseName}
                                                    </Link>
                                                </td>
                                                <td className="customTable">{row.series}</td>
                                                <td className="customTable">{row.repetitions}</td>
                                                <td className="customTable">{row.weight}</td>
                                                <td className="customTable edit">
                                                    <div className="form-buttons">
                                                        <Button className="primary template" title='Pulsa para editar fila' onClick={() => setEditingRowId(row.id)}>
                                                            <BsPencilSquare className="checkIconStyle" color='#e6af2e' size={20} />
                                                        </Button>
                                                        <Button className="primary template delete"
                                                            title='Pulsa para eliminar fila' onClick={() => handleDeleteRow(row)} >
                                                            <BsTrash className="crossIconStyle" color='red' size={20} />
                                                        </Button>
                                                    </div>
                                                </td>
                                            </>
                                        )}
                                    </tr>
                                ))}
                                <tr>
                                    {showAddInput ? (
                                        <td colSpan={5} className="customTable">
                                            <AddTemplateRow />
                                        </td>
                                    ) : (
                                        <td colSpan={5} className="customTable">
                                            <Button className="primary cycle" onClick={() => setShowAddInput(true)}>
                                                <BsFillPlusCircleFill className="plusIconStyle cycle" />
                                                <span>
                                                    <b><FormattedMessage id="project.templates.addTemplateRow" /></b>
                                                </span>
                                            </Button>
                                        </td>
                                    )}
                                </tr>
                            </tbody>
                        </Table>
                    </div>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Col>
                <Col xs={12} sm={12} md={3} className='comment'>
                    <h3 className="title comments">
                        Comentarios
                    </h3>
                </Col>

            </Row>

        </div >

    );

}

export default TemplateView;
