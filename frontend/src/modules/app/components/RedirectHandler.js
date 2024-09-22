import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const RedirectHandler = () => {

  const navigate = useNavigate();
  const path = window.location.hash;

  useEffect(() => {
    if (path === "#/users/clients") {
      navigate("/users/clients", { replace: true });
    } else if (path.startsWith("#/templates/trainingCycles/")) {
      navigate(path, { replace: true });
    } else {
      navigate("/notFound", { replace: true });
    }
  }, [path, navigate]);

  return null;
  
};

export default RedirectHandler;
