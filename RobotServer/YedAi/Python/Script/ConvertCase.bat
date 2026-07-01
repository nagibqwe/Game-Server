SETLOCAL enabledelayedexpansion
FOR /F "delims=" %%I IN (%DEST_ROOT%\DiagramFileMappingTemp.h) DO (
SET var=%%I
SET  a=!var:A=a!
SET  b=!a:B=b!
SET  c=!b:C=c!
SET  d=!c:D=d!
SET  e=!d:E=e!
SET  f=!e:F=f!
SET  g=!f:G=g!
SET  h=!g:H=h!
SET  i=!h:I=i!
SET  j=!i:J=g!
SET  k=!j:K=k!
SET  l=!k:L=l!
SET  m=!l:M=m!
SET  n=!m:N=n!
SET  o=!n:O=o!
SET  p=!o:P=p!
SET  q=!p:Q=q!
SET  r=!q:R=r!
SET  s=!r:S=s!
SET  t=!s:T=t!
SET  u=!t:U=u!
SET  v=!u:V=v!
SET  w=!v:W=w!
SET  x=!w:X=x!
SET  y=!x:Y=y!
SET  z=!y:Z=z!
echo !z!>>%DEST_ROOT%\DiagramFileMapping.h
)
DEL %DEST_ROOT%\DiagramFileMappingTemp.h
MOVE %DEST_ROOT%\DiagramFileMapping.h %DEST_SRC_ROOT%