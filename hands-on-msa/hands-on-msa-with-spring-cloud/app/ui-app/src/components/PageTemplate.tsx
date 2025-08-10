import React from 'react';

export type PageTemplateProps = {
  title: string;
  buttonArea : React.ReactNode;
  contentArea: React.ReactNode;
}
const PageTemplate = (
  {
    buttonArea,
    contentArea,
    title,
  }: PageTemplateProps
) => {
  return (
    <div>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
      }}>
        <h1>{title}</h1>
        {buttonArea}
      </div>
      <div>
        {contentArea}
      </div>
    </div>
  );
};

export default PageTemplate;